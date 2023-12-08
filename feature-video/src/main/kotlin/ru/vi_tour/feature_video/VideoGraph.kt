package ru.vi_tour.feature_video

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import ru.vi_tour.feature_video.screens.video.VideoScreen

object VideoGraph: Screen {
    @Composable
    override fun Content() {
        Navigator(screen = VideoScreen)
    }
}