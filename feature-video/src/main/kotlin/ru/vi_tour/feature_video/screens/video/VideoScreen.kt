package ru.vi_tour.feature_video.screens.video

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel

object VideoScreen: AndroidScreen() {
    @Composable
    override fun Content() {
        VideoScreenContent(vm = getViewModel())
    }
}