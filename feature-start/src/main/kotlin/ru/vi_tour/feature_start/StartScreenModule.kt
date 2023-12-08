package ru.vi_tour.feature_start

import cafe.adriel.voyager.core.registry.screenModule
import ru.vi_tour.core_navigation.StartNavSharing
import ru.vi_tour.feature_start.screens.splash.SplashScreen

val startScreenModule = screenModule {
    register<StartNavSharing.StartGraph> {
        StartGraph
    }
    register<StartNavSharing.SplashScreen> {
        SplashScreen
    }
}