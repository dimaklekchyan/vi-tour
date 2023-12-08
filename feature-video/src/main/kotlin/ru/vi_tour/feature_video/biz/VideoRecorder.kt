package ru.vi_tour.feature_video.biz

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.vi_tour.core.getAppName
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

data class VideoRecordStorageOptions(
    val storage: Storage,
    val outputName: String,
    val outputDir: String
) {

    init {
        if (outputName.isEmpty()) {
            throw IllegalArgumentException("Output name can't be empty")
        }
        if (outputDir.isEmpty()) {
            throw IllegalArgumentException("Output directory can't be empty")
        }
    }

    enum class Storage {
        Internal,
        External
    }

    companion object {
        fun getDefault(context: Context): VideoRecordStorageOptions {
            val date = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
                .format(System.currentTimeMillis())
            return VideoRecordStorageOptions(
                storage = Storage.Internal,
                outputName = "${context.getAppName()}_$date",
                outputDir = context.getAppName()
            )
        }
    }
}

class VideoRecorder(
    private val context: Context,
    private val storageOptions: VideoRecordStorageOptions,
    private val onProgress: (timeStr: String, seconds: Int) -> Unit = { _, _ -> },
    private val onSuccess: (Uri) -> Unit = {},
    private val onError: (e: Throwable?) -> Unit = {}
) {

    val controller = LifecycleCameraController(context).apply {
        setEnabledUseCases(CameraController.VIDEO_CAPTURE)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    }

    private val _isRecordingFlow = MutableStateFlow<Boolean>(false)
    val isRecordingFlow: StateFlow<Boolean> = _isRecordingFlow

    private var recording: Recording? = null

    fun stop() {
        recording?.stop()
        recording = null
    }

    @SuppressLint("MissingPermission")
    fun recordVideo(quality: Quality) {
        if(recording != null) {
            stop()
            return
        }

        controller.videoCaptureQualitySelector = QualitySelector.from(
            quality,
            FallbackStrategy.higherQualityOrLowerThan(Quality.FHD)
        )

        //TODO use wide angel camera

        when(storageOptions.storage) {
            VideoRecordStorageOptions.Storage.Internal -> {
                recording = controller.startRecording(
                    getInternalOutputOptions(storageOptions),
                    AudioConfig.create(true),
                    ContextCompat.getMainExecutor(context)
                ) { event ->
                    onEvent(event)
                }
            }
            VideoRecordStorageOptions.Storage.External -> {
                recording = controller.startRecording(
                    getExternalOutputOptions(storageOptions),
                    AudioConfig.create(true),
                    ContextCompat.getMainExecutor(context)
                ) { event ->
                    onEvent(event)
                }
            }
        }
    }

    private fun onEvent(
        event: VideoRecordEvent
    ) {
        when(event) {
            is VideoRecordEvent.Start -> {
                _isRecordingFlow.tryEmit(true)
            }
            is VideoRecordEvent.Status -> {
                val allSeconds = event.recordingStats.recordedDurationNanos / 1000 / 1000 / 1000
                val minutes = allSeconds / 60
                val seconds = allSeconds % 60
                val minutesStr = if(minutes < 10) "0$minutes" else minutes.toString()
                val secondsStr = if(seconds < 10) "0$seconds" else seconds.toString()
                onProgress("$minutesStr:$secondsStr", seconds.toInt())
            }
            is VideoRecordEvent.Finalize -> {
                _isRecordingFlow.tryEmit(false)
                if(event.hasError()) {
                    recording?.close()
                    recording = null
                    onError(event.cause)
                } else {
                    onSuccess(event.outputResults.outputUri)
                }
            }
        }
    }

    private fun getExternalOutputOptions(storageOptions: VideoRecordStorageOptions): MediaStoreOutputOptions {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, storageOptions.outputName)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.MediaColumns.SIZE, 0)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/${storageOptions.outputDir}")
            }
        }

        return MediaStoreOutputOptions
            .Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()
    }

    private fun getInternalOutputOptions(storageOptions: VideoRecordStorageOptions): FileOutputOptions {
        val outputFile = File(
            "${context.filesDir}/${storageOptions.outputDir}",
            "${storageOptions.outputName}.mp4"
        )
        outputFile.delete()
        return FileOutputOptions.Builder(outputFile).build()
    }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )

        const val PERMISSIONS_KEY = "camera_permissions"

        val QUALITIES = arrayOf(
            Quality.SD,
            Quality.HD,
            Quality.FHD,
            Quality.UHD
        )
    }
}

internal fun Quality.name() = when(this) {
    Quality.SD -> "SD"
    Quality.HD -> "HD"
    Quality.FHD -> "FHD"
    Quality.UHD -> "UHD"
    else -> throw IllegalArgumentException("$this is unsupported quality type")
}

@Composable
fun rememberVideoRecorder(
    storageOptions: VideoRecordStorageOptions = VideoRecordStorageOptions.getDefault(LocalContext.current),
    onProgress: (timeStr: String, seconds: Int) -> Unit = { _, _ -> },
    onSuccess: (Uri) -> Unit = {},
    onError: (e: Throwable?) -> Unit = {}
): VideoRecorder {
    val context = LocalContext.current
    return remember { VideoRecorder(context, storageOptions, onProgress, onSuccess, onError) }
}