package ru.vi_tour.feature_video.ui_components

import android.Manifest
import android.net.Uri
import androidx.annotation.RequiresPermission
import androidx.camera.video.Quality
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ru.vi_tour.design_system.theme.colorBlack
import ru.vi_tour.feature_video.biz.VideoRecordStorageOptions
import ru.vi_tour.feature_video.biz.VideoRecorder
import ru.vi_tour.feature_video.biz.name
import ru.vi_tour.feature_video.biz.rememberRotationController
import ru.vi_tour.feature_video.biz.rememberVideoRecorder

@RequiresPermission(allOf = [Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA])
@Composable
internal fun VideoCamera(
    modifier: Modifier = Modifier,
    storageOptions: VideoRecordStorageOptions = VideoRecordStorageOptions.getDefault(LocalContext.current),
    availableQualities: Array<Quality> = VideoRecorder.QUALITIES,
    onSuccess: (Uri) -> Unit = {},
    onError: (e: Throwable?) -> Unit = {}
) {

    var progress by remember { mutableStateOf("") }

    //TODO implement rotation line
    val rotationController = rememberRotationController()

    val videoRecorder = rememberVideoRecorder(
        storageOptions = storageOptions,
        onProgress = remember{{ timeStr, _ ->
            progress = timeStr
        }},
        onSuccess = onSuccess,
        onError = onError
    )

    var quality by remember(availableQualities) {
        mutableStateOf(availableQualities.lastOrNull() ?: Quality.FHD)
    }

    val isRecording = videoRecorder.isRecordingFlow.collectAsState(initial = false)

    DisposableEffect(key1 = Unit) {
        onDispose { videoRecorder.stop() }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorBlack)
            .systemBarsPadding()
    ) {
        VideoCameraPreview(
            modifier = Modifier.fillMaxSize(),
            controller = videoRecorder.controller
        )
        Header(
            modifier = Modifier.fillMaxWidth(),
            isRecording = isRecording.value,
            progress = progress,
            availableQualities = availableQualities,
            quality = quality,
            onQualityChosen = remember {{ quality = it }}
        )
        Footer(
            modifier = Modifier.fillMaxWidth(),
            videoRecorder = videoRecorder,
            quality = quality,
            isRecording = isRecording.value
        )
    }
}

@RequiresPermission(allOf = [Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA])
@Composable
internal fun VideoCameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = Unit) {
        onDispose {
            controller.unbind()
        }
    }

    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun BoxScope.Header(
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    progress: String,
    availableQualities: Array<Quality>,
    quality: Quality,
    onQualityChosen: (Quality) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .background(Color.Black)
            .statusBarsPadding()
            .align(Alignment.TopCenter)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(modifier = Modifier.weight(1f))
        ProgressBlock(
            modifier = Modifier.weight(1f),
            isRecording = isRecording,
            progress = progress
        )
        QualityBlock(
            modifier = Modifier.weight(1f),
            availableQualities = availableQualities,
            currentQuality = quality,
            onQualityChosen = onQualityChosen
        )
    }
}

@Composable
private fun BoxScope.Footer(
    modifier: Modifier = Modifier,
    videoRecorder: VideoRecorder,
    quality: Quality,
    isRecording: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .align(Alignment.BottomCenter),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(
                    onClick = remember(videoRecorder) {{
                        videoRecorder.recordVideo(quality)
                    }}
                ),
            contentAlignment = Alignment.Center
        ) {
            val size by animateDpAsState(
                targetValue = if(isRecording) 20.dp else 54.dp,
                label = ""
            )
            val cornerSize by animateDpAsState(
                targetValue = if(isRecording) 4.dp else 50.dp,
                label = ""
            )

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(cornerSize))
                    .background(Color.Red)
            )
        }
    }
}

@Composable
private fun ProgressBlock(
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    progress: String
) {
    if (isRecording && progress.isNotEmpty()) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = progress,
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
        }
    } else {
        Box(modifier = modifier)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun QualityBlock(
    modifier: Modifier = Modifier,
    availableQualities: Array<Quality>,
    currentQuality: Quality,
    onQualityChosen: (Quality) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier.width(100.dp),
        expanded = expanded,
        onExpandedChange = remember {{ expanded = !expanded }}
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = currentQuality.name(),
            color = Color.White,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        ExposedDropdownMenu(
            modifier = Modifier
                .width(100.dp)
                .background(Color.Black),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            availableQualities.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = remember {{
                        onQualityChosen(selectionOption)
                        expanded = false
                    }}
                ){
                    Text(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        text = selectionOption.name(),
                        style = MaterialTheme.typography.body1,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}