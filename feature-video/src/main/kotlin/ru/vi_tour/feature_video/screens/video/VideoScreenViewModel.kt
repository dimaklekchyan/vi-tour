package ru.vi_tour.feature_video.screens.video

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.vi_tour.core.BaseViewModel
import javax.inject.Inject

data class VideoScreenState(
    val loading: Boolean = false
)

sealed class VideoScreenEvent {

}

sealed class VideoScreenAction {

}

@HiltViewModel
class VideoScreenViewModel @Inject constructor(): BaseViewModel<VideoScreenState, VideoScreenAction, VideoScreenEvent>(
    initialState = VideoScreenState()
) {

    init {
        Log.d("TAG2", "VideoScreenViewModel init")
    }

    override fun obtainEvent(viewEvent: VideoScreenEvent) {
        TODO("Not yet implemented")
    }
}