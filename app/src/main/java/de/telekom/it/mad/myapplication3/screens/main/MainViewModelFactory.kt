package de.telekom.it.mad.myapplication3.screens.main

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.telekom.it.mad.myapplication3.data.AppDataRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val application: Application,
    private val appDataRepo: AppDataRepository
) : ViewModelProvider.NewInstanceFactory() {

    @NonNull
    @SuppressWarnings("unchecked")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(application, appDataRepo) as T
            else -> throw IllegalArgumentException("Non exist class!")
        }
    }
}