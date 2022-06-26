package de.telekom.it.mad.myapplication3.download

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.work.*
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class DownloadRequest(
    private val context: Context,
    workerParameters: WorkerParameters
) : RxWorker(context, workerParameters) {

    private val okhttp = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .callTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .build()

    override fun createWork(): Single<Result> {
        val url = inputData.getString("URL")
        val fileName = inputData.getString("FILENAME")

        return Single.create {
            if (url == null || fileName == null) {
                it.onError(IllegalStateException("url($url) or filename($fileName) are null"))
                return@create
            }
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()
                val response = okhttp.newCall(request).execute()
                val body = response.body
                if (response.isSuccessful && body != null) {
                    val inputStream: InputStream = body.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    saveFile(fileName, bitmap)
                    it.onSuccess(Result.success())
                } else {
                    it.onError(Exception("Response code: ${response.code}"));
                }
            } catch (e: IOException) {
                it.onError(e);
            }
        }
    }

    private fun saveFile(fileName: String, bitmap: Bitmap) {
        val file = File(context.cacheDir, fileName)
        val size: Int = bitmap.rowBytes * bitmap.height
        val byteBuffer: ByteBuffer = ByteBuffer.allocate(size)
        bitmap.copyPixelsToBuffer(byteBuffer)
        val byteArray = byteBuffer.array()
        file.writeBytes(byteArray)
    }
}