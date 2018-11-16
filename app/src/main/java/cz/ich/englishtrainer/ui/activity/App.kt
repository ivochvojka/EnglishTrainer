package cz.ich.englishtrainer.ui.activity

import android.app.Application
import cz.ich.englishtrainer.BuildConfig
import timber.log.Timber

/**
 * @author Ivo Chvojka
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}