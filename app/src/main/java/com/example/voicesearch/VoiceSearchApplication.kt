package com.example.voicesearch

import android.app.Application
import com.example.voicesearch.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VoiceSearchApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin

        startKoin{
            androidLogger()
            androidContext(this@VoiceSearchApplication)
            modules(listOf(appModule))
        }
//        Stetho.initializeWithDefaults(this)

    }
}