package ru.vi_tour.feature_start.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.vi_tour.design_system.components.AppScaffold
import ru.vi_tour.design_system.components.TransparentSystemBars

@Composable
internal fun SplashScreenContent() {

    TransparentSystemBars()

    AppScaffold {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Splash")
        }
    }
}