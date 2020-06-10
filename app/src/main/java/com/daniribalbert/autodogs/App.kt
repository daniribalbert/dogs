package com.daniribalbert.autodogs

import android.app.Application
import com.daniribalbert.autodogs.network.di.apiModule
import com.daniribalbert.autodogs.ui.main.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()

        // start Koin!
        startKoin {
            // declare used Android context
            androidContext(this@App)
            // declare modules
            modules(viewModelModule, apiModule)
        }
    }


    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}

