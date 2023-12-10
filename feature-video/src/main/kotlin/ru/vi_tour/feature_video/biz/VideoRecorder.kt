package ru.vi_tour.feature_video.biz

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CaptureRequest
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.camera2.impl.Camera2ImplConfig
import androidx.camera.camera2.internal.Camera2CameraControlImpl
import androidx.camera.camera2.internal.Camera2CameraInfoImpl
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.vi_tour.core.getAppName
import ru.vi_tour.data_video.domain.model.CameraConfig
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
        val date: String
            get() = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
            .format(System.currentTimeMillis())

        fun getDefault(context: Context): VideoRecordStorageOptions {
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
    private val onError: (e: Throwable?) -> Unit = {},
) {

    var quality by mutableStateOf<Quality>(Quality.HIGHEST)
        private set
    var lens by mutableStateOf<Int>(CameraSelector.LENS_FACING_BACK)
        private set
    var config: CameraConfig by mutableStateOf(CameraConfig())
        private set


    @SuppressLint("RestrictedApi")
    val controller = LifecycleCameraController(context).apply {
        setEnabledUseCases(CameraController.VIDEO_CAPTURE)
        videoCaptureQualitySelector = QualitySelector.from(
            quality,
            FallbackStrategy.higherQualityOrLowerThan(Quality.FHD)
        )
        setWideAngleLens()

        isPinchToZoomEnabled = false
    }

    private val _isRecordingFlow = MutableStateFlow<Boolean>(false)
    val isRecordingFlow: StateFlow<Boolean> = _isRecordingFlow

    private var recording: Recording? = null

    fun stop() {
        recording?.stop()
        recording = null
        _isRecordingFlow.tryEmit(false)
    }

    fun setNewQuality(quality: Quality) {
        this.quality = quality
        controller.videoCaptureQualitySelector = QualitySelector.from(
            this.quality,
            FallbackStrategy.higherQualityOrLowerThan(Quality.FHD)
        )
    }
    fun setNewLens(lensFacing: Int) {
        this.lens = lensFacing
        controller.cameraSelector = CameraSelector.Builder()
            .requireLensFacing(this.lens)
            .build()
    }

    @SuppressLint("RestrictedApi")
    private fun LifecycleCameraController.setWideAngleLens() {
        cameraSelector = CameraSelector.Builder()
            .addCameraFilter { cameraInfos ->
                // filter back cameras with minimum sensor pixel size
                val backCameras = cameraInfos.filterIsInstance<Camera2CameraInfoImpl>()
                    .filter {
                        val pixelWidth = it.cameraCharacteristicsCompat.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)?.width ?: 0
                        it.lensFacing == CameraSelector.LENS_FACING_BACK && pixelWidth >= 2000 // arbitrary number resolved empirically
                    }

                // try to find wide lens camera, if not present, default to general backCameras
                backCameras.minByOrNull {
                    val focalLengths = it.cameraCharacteristicsCompat.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
                    focalLengths?.getOrNull(0) ?: 0f
                }
                    ?.let { listOf(it) } ?: backCameras
            }
            .build()
    }

    @SuppressLint("MissingPermission")
    fun recordVideo() {
        if(recording != null) {
            stop()
            return
        }

        turnOnStabilization()
        setMinimalZoom()
        updateConfig()

        when(storageOptions.storage) {
            VideoRecordStorageOptions.Storage.Internal -> {
                recording = controller.startRecording(
                    getInternalOutputOptions(storageOptions),
                    AudioConfig.create(false),
                    ContextCompat.getMainExecutor(context)
                ) { event ->
                    onEvent(event)
                }
            }
            VideoRecordStorageOptions.Storage.External -> {
                recording = controller.startRecording(
                    getExternalOutputOptions(storageOptions),
                    AudioConfig.create(false),
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

    @SuppressLint("RestrictedApi")
    private fun updateConfig() {
        controller.cameraInfo?.let { info ->
            val resolution = QualitySelector.getResolution(info, quality)
            config = config.copy(
                lens = lens.name(),
                quality = quality.name(),
                resolutionHeight = resolution?.height ?: 0,
                resolutionWidth = resolution?.width ?: 0,
                zoom = info.zoomState.value?.zoomRatio ?: 0f,
            )
        }
    }

    @SuppressLint("RestrictedApi")
    private fun turnOnStabilization() {
        val configBuilder = Camera2ImplConfig.Builder()
        configBuilder.setCaptureRequestOption(
            CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE,
            CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE_ON
        )
        configBuilder.setCaptureRequestOption(
            CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE,
            CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_ON
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            configBuilder.setCaptureRequestOption(
                CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE,
                CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_PREVIEW_STABILIZATION
            )
            configBuilder.setCaptureRequestOption(
                CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE,
                CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_PREVIEW_STABILIZATION
            )
        }
        (controller.cameraControl as? Camera2CameraControlImpl)?.addInteropConfig(configBuilder.build())
    }

    private fun setMinimalZoom() {
        controller.setLinearZoom(0f)
        controller.setZoomRatio(0f)
        controller.cameraControl?.setLinearZoom(0f)
        controller.cameraControl?.setZoomRatio(0f)
    }

    companion object {
        val PERMISSION = Manifest.permission.CAMERA

        const val PERMISSIONS_KEY = "camera_permissions"

        val QUALITIES = arrayOf(
            Quality.SD,
            Quality.HD,
            Quality.FHD,
            Quality.UHD,
            Quality.HIGHEST,
            Quality.LOWEST
        )
        val LENSES = arrayOf(
            CameraSelector.LENS_FACING_BACK,
            CameraSelector.LENS_FACING_FRONT,
            CameraSelector.LENS_FACING_UNKNOWN
        )
    }
}

internal fun Quality.name() = when(this) {
    Quality.SD -> "SD"
    Quality.HD -> "HD"
    Quality.FHD -> "FHD"
    Quality.UHD -> "UHD"
    Quality.HIGHEST -> "Highest"
    Quality.LOWEST -> "Lowest"
    else -> throw IllegalArgumentException("$this is unsupported quality type")
}

internal fun Int.name() = when(this) {
    CameraSelector.LENS_FACING_BACK -> "Back"
    CameraSelector.LENS_FACING_FRONT -> "Front"
    CameraSelector.LENS_FACING_UNKNOWN -> "Unknown"
    else -> throw IllegalArgumentException("$this is unsupported lens type")
}

@Composable
fun rememberVideoRecorder(
    storageOptions: VideoRecordStorageOptions = VideoRecordStorageOptions.getDefault(LocalContext.current),
    onProgress: (timeStr: String, seconds: Int) -> Unit = { _, _ -> },
    onSuccess: (Uri) -> Unit = {},
    onError: (e: Throwable?) -> Unit = {},
): VideoRecorder {
    val context = LocalContext.current
    return remember { VideoRecorder(context, storageOptions, onProgress, onSuccess, onError) }
}