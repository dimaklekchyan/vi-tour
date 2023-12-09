package ru.vi_tour.feature_video.ui_components

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.camera.video.Quality
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ru.vi_tour.data_video.domain.model.CameraConfig
import ru.vi_tour.design_system.theme.colorBlack
import ru.vi_tour.design_system.theme.colorGreen
import ru.vi_tour.design_system.theme.colorRed
import ru.vi_tour.design_system.theme.colorYellow
import ru.vi_tour.feature_video.biz.VideoRecordStorageOptions
import ru.vi_tour.feature_video.biz.VideoRecorder
import ru.vi_tour.feature_video.biz.name
import ru.vi_tour.feature_video.biz.rememberRotationController
import ru.vi_tour.feature_video.biz.rememberVideoRecorder
import kotlin.math.abs

@RequiresPermission(allOf = [Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA])
@Composable
internal fun VideoCamera(
    modifier: Modifier = Modifier,
    storageOptions: VideoRecordStorageOptions = VideoRecordStorageOptions.getDefault(LocalContext.current),
    availableQualities: Array<Quality> = VideoRecorder.QUALITIES,
    availableLenses: Array<Int> = VideoRecorder.LENSES,
    onSuccess: (Uri) -> Unit = {},
    onError: (e: Throwable?) -> Unit = {},
    onConfigChanged: (config: CameraConfig) -> Unit = {},
) {

    var progress by remember { mutableStateOf("") }

    val videoRecorder = rememberVideoRecorder(
        storageOptions = storageOptions,
        onProgress = remember{{ timeStr, _ ->
            progress = timeStr
        }},
        onSuccess = remember {{ uri ->
            progress = ""
            onSuccess(uri)
        }},
        onError = remember{{ ex ->
            progress = ""
            onError(ex)
        }}
    )

    val isRecording = videoRecorder.isRecordingFlow.collectAsState(initial = false)

    DisposableEffect(key1 = Unit) {
        onDispose { videoRecorder.stop() }
    }

    LaunchedEffect(key1 = videoRecorder.config) {
        onConfigChanged(videoRecorder.config)
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorBlack)
            .systemBarsPadding()
    ) {
        VideoCameraPreview(
            modifier = Modifier.fillMaxSize(),
            quality = videoRecorder.quality,
            lensFacing = videoRecorder.lens,
            controller = videoRecorder.controller
        )
        Header(
            modifier = Modifier.fillMaxWidth(),
            isRecording = isRecording.value,
            progress = progress,
            availableQualities = availableQualities,
            availableLenses = availableLenses,
            quality = videoRecorder.quality,
            lens = videoRecorder.lens,
            onQualityChosen = videoRecorder::setNewQuality,
            onLensChosen = videoRecorder::setNewLens,
        )
        Footer(
            modifier = Modifier.fillMaxWidth(),
            videoRecorder = videoRecorder,
            isRecording = isRecording.value
        )
        RotationLevel()
    }
}

@RequiresPermission(allOf = [Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA])
@Composable
internal fun VideoCameraPreview(
    controller: LifecycleCameraController,
    quality: Quality,
    lensFacing: Int,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    //TODO recording stops due to changing of lensFacing and quality
    val previewView = remember(controller, quality, lensFacing) {
        PreviewView(context).apply {
            this.controller = controller
        }
    }

    LaunchedEffect(key1 = controller, key2 = quality, key3 = lensFacing) {
        controller.unbind()
        controller.bindToLifecycle(lifecycleOwner)
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            controller.unbind()
        }
    }

    AndroidView(
        factory = {
            previewView
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
    availableLenses: Array<Int>,
    quality: Quality,
    lens: Int,
    onQualityChosen: (Quality) -> Unit,
    onLensChosen: (Int) -> Unit
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
        if (availableLenses.size > 1) {
            LensBlock(
                modifier = Modifier.weight(1f),
                lenses = availableLenses,
                currentLens = lens,
                onLensChosen = onLensChosen
            )
        } else {
            Box(modifier = Modifier.weight(1f))
        }
        ProgressBlock(
            modifier = Modifier.weight(1f),
            isRecording = isRecording,
            progress = progress
        )
        if (availableQualities.size > 1) {
            QualityBlock(
                modifier = Modifier.weight(1f),
                availableQualities = availableQualities,
                currentQuality = quality,
                onQualityChosen = onQualityChosen
            )
        } else {
            Box(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun BoxScope.Footer(
    modifier: Modifier = Modifier,
    videoRecorder: VideoRecorder,
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
                .clickable(onClick = videoRecorder::recordVideo),
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LensBlock(
    modifier: Modifier = Modifier,
    lenses: Array<Int>,
    currentLens: Int,
    onLensChosen: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier.width(100.dp),
        expanded = expanded,
        onExpandedChange = remember {{ expanded = !expanded }}
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = currentLens.name(),
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
            lenses.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = remember {{
                        onLensChosen(selectionOption)
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

@Composable
private fun BoxScope.RotationLevel() {

    val rotationController = rememberRotationController()

    val localDensity = LocalDensity.current

    val rollDeviationColor by animateColorAsState(
        targetValue = when(abs(rotationController.roll)) {
            in 0f..20f -> colorGreen
            in 20f..40f -> colorYellow
            else -> colorRed
        },
        label = "",
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    val pitchDeviationColor by animateColorAsState(
        targetValue = when(abs(rotationController.pitch)) {
            in 0f..20f -> colorGreen
            in 20f..40f -> colorYellow
            else -> colorRed
        },
        label = "",
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .height(2.dp)
            .padding(horizontal = 10.dp)
            .background(colorGreen)
    )

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .height(2.dp)
            .padding(horizontal = 10.dp)
            .rotate(rotationController.roll)
            .background(rollDeviationColor)
    )



    Canvas(
        modifier = Modifier
            .align(Alignment.CenterStart)
            .fillMaxHeight()
            .padding(start = 10.dp),
        onDraw = {

            val halfY = size.height / 2
            val deviationOffset = (halfY / 100) * rotationController.pitch
            val deviationWidth = with(localDensity) { 2.dp.toPx() }

            drawLine(
                color = pitchDeviationColor,
                start = Offset(0f, halfY),
                end = Offset(0f, halfY + deviationOffset),
                strokeWidth = deviationWidth
            )
        }
    )
}