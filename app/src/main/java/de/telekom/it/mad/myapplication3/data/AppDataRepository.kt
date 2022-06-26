package de.telekom.it.mad.myapplication3.data

import android.content.Context
import de.telekom.it.mad.myapplication3.R
import de.telekom.it.mad.myapplication3.download.Downloader
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataRepository
@Inject
constructor(
    private val downloader: Downloader,
    private val context: Context
) {

    private val list = listOf<AppData>(
        ImageAppData("https://raw.githubusercontent.com/wiki/Reactiv2eX/RxJava/images/rx-operators/merge.png"),
        TextAppData(R.string.app_name),
        ImageAppData("https://habrastorage.org/getpro/habr/post_images/0c4/44d/2ac/0c444d2ac478bfde7f57a8ca91eda1cf.png"),
        TextAppData(R.string.app_name)
    )

    fun getAppDataObservable(): Observable<List<AppData>> {
        return Observable.fromIterable(list)
            .flatMap { data ->
                when (data) {
                    is ImageAppData -> getImageSingle(data).toObservable() as Observable<AppData>
                    is TextAppData -> Observable.just(data.apply { string = context.getString(data.id) })
                }
            }
            .toList()
            .toObservable()
    }

    private fun getImageSingle(data: ImageAppData): Single<ImageAppData> {
        return downloader.downloadImageDirectly(data.url)
            .retry(3)
            .map { data.apply { bitmap = it.bitmap } }
            .onErrorReturn { data.apply { error = it.message ?: "Unknown error" } }
    }
}