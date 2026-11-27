package org.apagar.androidApp

import android.app.Application
import org.apagar.shared.di.initKoin
import org.apagar.shared.di.initLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initLogger()

        initKoin {
            androidLogger()
            androidContext(this@AndroidApp)
        }
    }
}
