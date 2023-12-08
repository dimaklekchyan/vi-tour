package ru.vi_tour

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import dagger.hilt.android.HiltAndroidApp
import ru.vi_tour.feature_start.startScreenModule
import ru.vi_tour.feature_video.videoScreenModule

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        ScreenRegistry {
            startScreenModule()
            videoScreenModule()
        }
    }
}