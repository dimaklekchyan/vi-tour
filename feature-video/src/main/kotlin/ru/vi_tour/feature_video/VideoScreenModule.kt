package ru.vi_tour.feature_video

import cafe.adriel.voyager.core.registry.screenModule
import ru.vi_tour.core_navigation.VideoNavSharing
import ru.vi_tour.feature_video.screens.video.VideoScreen

val videoScreenModule = screenModule {
    register<VideoNavSharing.VideoGraph> {
        VideoGraph
    }
    register<VideoNavSharing.VideoScreen> {
        VideoScreen
    }
}