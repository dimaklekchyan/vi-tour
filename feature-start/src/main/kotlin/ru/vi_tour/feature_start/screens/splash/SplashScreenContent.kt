package ru.vi_tour.feature_start.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import ru.vi_tour.core_navigation.VideoNavSharing
import ru.vi_tour.design_system.components.AppScaffold
import ru.vi_tour.design_system.components.TransparentSystemBars

@Composable
internal fun SplashScreenContent() {

    val navigator = LocalNavigator.currentOrThrow
    val videoGraph = rememberScreen(provider = VideoNavSharing.VideoGraph)

    TransparentSystemBars()

    LaunchedEffect(key1 = Unit) {
        delay(2000)
        navigator.replace(videoGraph)
    }

    AppScaffold {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Splash")
        }
    }
}