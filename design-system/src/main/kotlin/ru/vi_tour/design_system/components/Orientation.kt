package ru.vi_tour.design_system.components

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import ru.vi_tour.core.findActivity

@Composable
private fun ForceOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}

@Composable
fun ForcePortraitOrientation() {
    ForceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
}

@Composable
fun ForceLandscapeOrientation() {
    ForceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
}

@Composable
fun ForceUnspecifiedOrientation() {
    ForceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
}