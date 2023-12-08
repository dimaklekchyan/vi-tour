package ru.vi_tour.core_navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class VideoNavSharing: ScreenProvider {
    data object VideoGraph: VideoNavSharing()
    data object VideoScreen: VideoNavSharing()
}

sealed class StartNavSharing: ScreenProvider {
    data object StartGraph: StartNavSharing()
    data object SplashScreen: StartNavSharing()
}