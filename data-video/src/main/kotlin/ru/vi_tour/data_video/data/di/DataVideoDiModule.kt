package ru.vi_tour.data_video.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.vi_tour.data_video.data.network.service.VideoApiService
import ru.vi_tour.data_video.data.repository.VideoRepository
import ru.vi_tour.data_video.domain.repository.IVideoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataVideoDiModule {

    @Provides
    @Singleton
    internal fun provideVideoApiService(retrofit: Retrofit) = retrofit.create(VideoApiService::class.java)

    @Provides
    @Singleton
    internal fun bindVideoRepository(r: VideoRepository): IVideoRepository = r

}