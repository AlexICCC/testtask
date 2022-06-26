package de.telekom.it.mad.myapplication3.screens.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.telekom.it.mad.myapplication3.data.AppData
import de.telekom.it.mad.myapplication3.data.AppDataRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val application: Application,
    private val repo: AppDataRepository
) : ViewModel() {

    private val _dataList: MutableLiveData<List<AppData>> = MutableLiveData(mutableListOf())
    val dataList: LiveData<List<AppData>> = _dataList

    private val disposable: Disposable

    init {
        disposable = repo.getAppDataObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _dataList.value = it
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}