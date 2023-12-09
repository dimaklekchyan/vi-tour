package ru.vi_tour.data_video.domain.repository

import android.net.Uri
import ru.vi_tour.core.Either
import ru.vi_tour.data_video.domain.model.CameraConfig

interface IVideoRepository {
    suspend fun uploadVideo(
        videoUri: Uri,
        cameraData: CameraConfig
    ): Either<Either.None>
    suspend fun deleteVideo(
        videoUri: Uri
    )
}