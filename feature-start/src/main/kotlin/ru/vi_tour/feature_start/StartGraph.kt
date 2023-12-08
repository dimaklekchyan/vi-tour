package ru.vi_tour.feature_start

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import ru.vi_tour.feature_start.screens.splash.SplashScreen

object StartGraph: Screen {
    @Composable
    override fun Content() {
        Navigator(screen = SplashScreen)
    }
}