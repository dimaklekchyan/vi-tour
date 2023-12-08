package ru.vi_tour.feature_video.screens.video

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.vi_tour.core.BaseViewModel
import ru.vi_tour.data_video.domain.model.Video
import javax.inject.Inject

data class VideoScreenState(
    val video: Video? = null,
    val permissionsGranted: Boolean = false,
    val showPermissionsRationale: Boolean = false,
    val showUploadingDialog: Boolean = false,
    val loading: Boolean = false
)

sealed class VideoScreenEvent {
    data object WantToRecordVideo: VideoScreenEvent()
    data object PermissionsGranted: VideoScreenEvent()
    data object ShowPermissionsRationale: VideoScreenEvent()
    data object HidePermissionsRationale: VideoScreenEvent()
    class VideoRecorder(val video: Video): VideoScreenEvent()
    data object ConfirmUploading: VideoScreenEvent()
    data object DismissUploading: VideoScreenEvent()
}

sealed class VideoScreenAction {
    data object AskForPermissions: VideoScreenAction()
}

@HiltViewModel
class VideoScreenViewModel @Inject constructor(

): BaseViewModel<VideoScreenState, VideoScreenAction, VideoScreenEvent>(
    initialState = VideoScreenState()
) {

    override fun obtainEvent(viewEvent: VideoScreenEvent) {
        when(viewEvent) {
            is VideoScreenEvent.WantToRecordVideo -> {
                viewAction = VideoScreenAction.AskForPermissions
            }
            is VideoScreenEvent.PermissionsGranted -> {
                viewState = viewState.copy(permissionsGranted = true)
            }
            is VideoScreenEvent.ShowPermissionsRationale -> {
                viewState = viewState.copy(showPermissionsRationale = true)
            }
            is VideoScreenEvent.HidePermissionsRationale -> {
                viewState = viewState.copy(showPermissionsRationale = false)
            }
            is VideoScreenEvent.VideoRecorder -> {
                viewState = viewState.copy(video = viewEvent.video, showUploadingDialog = true)
            }
            is VideoScreenEvent.ConfirmUploading -> {
                viewState = viewState.copy(video = null, showUploadingDialog = false)
                sendVideo()
            }
            is VideoScreenEvent.DismissUploading -> {
                viewState = viewState.copy(video = null, showUploadingDialog = false)
                deleteVideo()
            }
        }
    }

    private fun sendVideo() {
        viewState.video?.let {
            //TODO send video
        }
    }

    private fun deleteVideo() {
        viewState.video?.let {
            //TODO delete video
        }
    }
}