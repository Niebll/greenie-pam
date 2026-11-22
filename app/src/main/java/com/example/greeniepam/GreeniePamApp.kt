package com.example.greeniepam

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.example.greeniepam.di.appModule
import com.example.greeniepam.feature.auth.authModule
import com.example.greeniepam.feature.detail_post.detailPostModule
import com.example.greeniepam.feature.home.homeModule
import com.example.greeniepam.feature.journal.journalModule
import com.example.greeniepam.feature.profile.profileModule
import com.example.greeniepam.feature.pustaka.pustakaModule
import com.example.greeniepam.feature.schedule.scheduleModule
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GreeniePamApp : Application(), SingletonImageLoader.Factory {
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
                pustakaModule,
            )
        }
    }

    /**
     * Loader gambar Coil untuk seluruh aplikasi.
     * Wikimedia (sumber foto Pustaka) mewajibkan User-Agent yang jelas,
     * kalau tidak permintaan foto bisa ditolak.
     */
    override fun newImageLoader(context: PlatformContext): ImageLoader {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().header("User-Agent", USER_AGENT).build())
            }
            .build()
        return ImageLoader.Builder(context)
            .components { add(OkHttpNetworkFetcherFactory(callFactory = { client })) }
            .crossfade(true)
            .build()
    }

    private companion object {
        const val USER_AGENT = "GreenieApp/1.0 (Android; student project)"
    }
}
