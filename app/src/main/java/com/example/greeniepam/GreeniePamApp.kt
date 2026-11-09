package com.example.greeniepam

import android.app.Application
import com.example.greeniepam.di.appModule
import com.example.greeniepam.feature.profile.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GreeniePamApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GreeniePamApp)
            modules(appModule, profileModule)
        }
    }
}
