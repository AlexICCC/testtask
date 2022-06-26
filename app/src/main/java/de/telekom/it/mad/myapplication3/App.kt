package de.telekom.it.mad.myapplication3

import android.app.Application
import de.telekom.it.mad.myapplication3.dagger.AppComponent
import de.telekom.it.mad.myapplication3.dagger.AppModule
import de.telekom.it.mad.myapplication3.dagger.DaggerAppComponent

class App: Application() {

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}