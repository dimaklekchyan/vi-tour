package ru.vi_tour.data_video.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CameraConfig(
    val lens: String = "",
    val quality: String = "",
    val resolutionHeight: Int = 0,
    val resolutionWidth: Int = 0,
    val zoom: Float = 0.0f
)
