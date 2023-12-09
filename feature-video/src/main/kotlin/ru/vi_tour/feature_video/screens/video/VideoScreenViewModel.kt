package ru.vi_tour.feature_video.screens.video

import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vi_tour.core.AppError
import ru.vi_tour.core.BaseViewModel
import ru.vi_tour.data_video.domain.model.CameraConfig
import ru.vi_tour.data_video.domain.repository.IVideoRepository
import javax.inject.Inject

data class VideoScreenState(
    val videoUri: Uri? = null,
    val permissionsGranted: Boolean = false,
    val showPermissionsRationale: Boolean = false,
    val showUploadingDialog: Boolean = false,
    val showSuccessfulDialog: Boolean = false,
    val loading: Boolean = false,
    val sendingError: AppError? = null,
    val cameraConfig: CameraConfig = CameraConfig()
)

sealed class VideoScreenEvent {
    data object WantToRecordVideo: VideoScreenEvent()
    data object PermissionsGranted: VideoScreenEvent()
    data object ShowPermissionsRationale: VideoScreenEvent()
    data object HidePermissionsRationale: VideoScreenEvent()
    class VideoRecorder(val videoUri: Uri): VideoScreenEvent()
    data object ConfirmUploading: VideoScreenEvent()
    data object DismissUploading: VideoScreenEvent()
    data object ClickOnSendingError: VideoScreenEvent()
    data object HideSuccessfulDialog: VideoScreenEvent()
    class CameraConfigChanged(val config: CameraConfig): VideoScreenEvent()
}

sealed class VideoScreenAction {
    data object AskForPermissions: VideoScreenAction()
}

@HiltViewModel
class VideoScreenViewModel @Inject constructor(
    private val repository: IVideoRepository
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
                viewState = viewState.copy(videoUri = viewEvent.videoUri, showUploadingDialog = true)
            }
            is VideoScreenEvent.ConfirmUploading -> {
                viewState = viewState.copy(showUploadingDialog = false)
                sendVideo()
            }
            is VideoScreenEvent.DismissUploading -> {
                viewState = viewState.copy(showUploadingDialog = false)
                deleteVideo()
            }
            is VideoScreenEvent.ClickOnSendingError -> {
                viewState = viewState.copy(sendingError = null)
                sendVideo()
            }
            is VideoScreenEvent.CameraConfigChanged -> {
                viewState = viewState.copy(cameraConfig = viewEvent.config)
            }
            is VideoScreenEvent.HideSuccessfulDialog -> {
                viewState = viewState.copy(showSuccessfulDialog = false)
            }
        }
    }

    private fun sendVideo() {
        viewState.videoUri?.let {
            viewModelScope.launch {
                viewState = viewState.copy(loading = true)
                val result = withContext(Dispatchers.IO) {
                    repository.uploadVideo(it, viewState.cameraConfig)
                }

                if (result.isSuccess()) {
                    viewState = viewState.copy(loading = false, videoUri = null, showSuccessfulDialog = true)
                }
                if (result.isError()) {
                    viewState = viewState.copy(loading = false, sendingError = result.error)
                }
            }
        }
    }

    private fun deleteVideo() {
        viewState.videoUri?.let { uri ->
            viewModelScope.launch {
                repository.deleteVideo(uri)
                viewState = viewState.copy(videoUri = null)
            }
        }
    }
}