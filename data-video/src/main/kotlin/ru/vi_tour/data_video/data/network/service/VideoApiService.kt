package ru.vi_tour.data_video.data.network.service

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.vi_tour.data_video.data.network.model.UploadVideoResponse
import java.io.File

internal interface VideoApiService {

    @Multipart
    @POST("video/add/")
    suspend fun uploadVideo(
        @Header("Authorization") token: String,
        @Part videoFile: MultipartBody.Part,
        @Part cameraData: MultipartBody.Part
    ): Response<UploadVideoResponse>
}