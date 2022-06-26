package de.telekom.it.mad.myapplication3.download

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Downloader
@Inject
constructor(
    private val context: Context,
    private val client: OkHttpClient
) {

    private val wm = WorkManager.getInstance(context)

    fun downloadImageByWorkManager(url: String, position: Int): Single<DownloadResult> {
        val fileName = "file$position"
        val tag = fileName
        val data = workDataOf(
            "FILENAME" to fileName,
            "URL" to url
        )

        return Single.create<DownloadResult> {
            val bitmap = getSavedBitmap(fileName)
            if (bitmap != null) {
                it.onSuccess(DownloadResult(bitmap))
                return@create
            }

//            val workStatus = getShedulledWork(tag)
//            if (workStatus != null) {
//                workStatus.observeForever { workinfo ->
//                    if (workinfo.state == WorkInfo.State.SUCCEEDED) {
//                        val bitmap = getSavedBitmap(fileName)!!
//                        it.onSuccess(DownloadResult(bitmap))
//                    }
//                }
//                return@create
//            }

            val request = OneTimeWorkRequestBuilder<DownloadRequest>()
                .setInputData(data)
                .addTag(tag)
                .build()

            wm.enqueue(request)
            it.onError(Exception("Not ready"))

//            val workStatus2 = getShedulledWork(tag)
//            if (workStatus2 != null) {
//                workStatus2.observeForever { workinfo ->
//                    if (workinfo.state == WorkInfo.State.SUCCEEDED) {
//                        val bitmap = getSavedBitmap(fileName)!!
//                        it.onSuccess(DownloadResult(bitmap))
//                    }
//                }
//                return@create
//            }
        }
    }

    private fun getSavedBitmap(fileName: String): Bitmap? {
        val file = File(context.cacheDir, fileName)
        if (!file.exists()) {
            return null
        }
        val bytes = file.readBytes()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun getShedulledWork(tag: String): LiveData<WorkInfo>? {
        val statuses: ListenableFuture<List<WorkInfo>?> = wm.getWorkInfosByTag(tag)
        val infos = statuses.get()
        if (infos == null || infos.isEmpty()) {
            return null
        }
        val id = infos.first().id
        return wm.getWorkInfoByIdLiveData(id)
    }

    fun downloadImageDirectly(url: String): Single<DownloadResult> {
        return Single.create<DownloadResult> {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()
                val response = client.newCall(request).execute()
                val body = response.body
                if (response.isSuccessful && body != null) {
                    val inputStream: InputStream = body.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    it.onSuccess(DownloadResult(bitmap))
                } else {
                    it.onError(Exception("Response code: ${response.code}"));
                }
            } catch (e: IOException) {
                it.onError(e)
            }
        }
    }

    class DownloadResult(
        val bitmap: Bitmap
    )
}