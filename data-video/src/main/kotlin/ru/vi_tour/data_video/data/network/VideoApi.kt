package ru.vi_tour.data_video.data.network

import android.util.Log
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import ru.vi_tour.core.Either
import ru.vi_tour.core_network.BaseApi
import ru.vi_tour.data_video.data.network.model.UploadVideoResponse
import ru.vi_tour.data_video.data.network.service.VideoApiService
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class VideoApi @Inject constructor(
    private val service: VideoApiService
): BaseApi() {

    suspend fun uploadVideo(
        token: String,
        videoFile: File,
        cameraData: String
    ): Either<UploadVideoResponse> {
        delay(2000)
        return doRequest(
            tag = "uploadVideo",
            request = {
                val videoFileBody = MultipartBody.Part.createFormData(
                    name = "video",
                    filename = videoFile.name,
                    body = videoFile.asRequestBody()
                )
                val cameraDataBody = MultipartBody.Part.createFormData(
                    name = "camera_data",
                    value = cameraData
                )
                service.uploadVideo(token, videoFileBody, cameraDataBody)
            },
            mapper = {
                it
            }
        )
    }
}