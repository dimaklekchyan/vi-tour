package ru.vi_tour.feature_video.screens.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import ru.vi_tour.design_system.components.AppScaffold
import ru.vi_tour.design_system.components.TransparentSystemBars
import ru.vi_tour.design_system.theme.AppTheme
import ru.vi_tour.design_system.theme.colorGreen
import ru.vi_tour.design_system.theme.colorRed
import ru.vi_tour.feature_video.biz.rememberRotationController

@Composable
internal fun VideoScreenContent(vm: VideoScreenViewModel) {
    TransparentSystemBars()

    val rotationController = rememberRotationController()

    AppScaffold {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(colorGreen)
            )
            Box(
                modifier = Modifier
                    .rotate(rotationController.roll)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(colorRed)
            )
        }
    }
}