package ru.vi_tour.feature_start.screens.splash

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen

object SplashScreen: AndroidScreen() {
    @Composable
    override fun Content() {
        SplashScreenContent()
    }
}