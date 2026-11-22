package com.example.greeniepam

import android.app.Application
import com.example.greeniepam.di.appModule
import com.example.greeniepam.feature.auth.authModule
import com.example.greeniepam.feature.detail_post.detailPostModule
import com.example.greeniepam.feature.home.homeModule
import com.example.greeniepam.feature.journal.journalModule
import com.example.greeniepam.feature.profile.profileModule
import com.example.greeniepam.feature.schedule.scheduleModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GreeniePamApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GreeniePamApp)
            modules(
                appModule,
                authModule,
                homeModule,
                detailPostModule,
                journalModule,
                scheduleModule,
                profileModule,
            )
        }
    }
}
