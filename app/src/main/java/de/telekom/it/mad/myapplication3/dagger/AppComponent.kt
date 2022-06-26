package de.telekom.it.mad.myapplication3.dagger

import dagger.Component
import de.telekom.it.mad.myapplication3.screens.main.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(a: MainActivity)
}