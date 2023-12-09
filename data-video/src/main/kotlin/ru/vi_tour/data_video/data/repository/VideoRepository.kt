package ru.vi_tour.data_video.data.repository

import android.net.Uri
import android.util.Log
import kotlinx.serialization.encodeToString
import ru.vi_tour.core.AppError
import ru.vi_tour.core.AppJsonConfiguration
import ru.vi_tour.core.Either
import ru.vi_tour.data_video.data.network.VideoApi
import ru.vi_tour.data_video.domain.model.CameraConfig
import ru.vi_tour.data_video.domain.repository.IVideoRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class VideoRepository @Inject constructor(
    private val api: VideoApi
): IVideoRepository {
    override suspend fun uploadVideo(
        videoUri: Uri,
        cameraData: CameraConfig
    ): Either<Either.None> {
        val result = videoUri.path?.let { path ->
            api.uploadVideo(
                token = "Token 7a18197f9e54f4637ee996449354d59f1ee6b232",
                videoFile = File(path),
                cameraData = AppJsonConfiguration.json.encodeToString(cameraData)
            )
        } ?: Either.error(AppError.FileDoesntExist())

        return if (result.isSuccess()) {
            deleteVideo(videoUri)
            Either.success(Either.None)
        } else {
            Either.error(result.error ?: AppError.Unknown())
        }
    }

    override suspend fun deleteVideo(videoUri: Uri) {
        try {
            videoUri.path?.let { path ->
                File(path).delete()
            }
        } catch (ex: IOException) {
            Log.d("TAG2", "File doesn't exist")
        }
    }
}