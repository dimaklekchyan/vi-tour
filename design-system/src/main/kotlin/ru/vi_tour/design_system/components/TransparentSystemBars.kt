package ru.vi_tour.design_system.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.vi_tour.design_system.theme.colorTransparent

@Composable
fun TransparentSystemBars(statusBarDarkIcons: Boolean = true, navBarDarkIcons: Boolean = true) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, statusBarDarkIcons, navBarDarkIcons) {
        systemUiController
            .setStatusBarColor(
                color = colorTransparent,
                darkIcons = statusBarDarkIcons
            )
        systemUiController
            .setNavigationBarColor(
                color = colorTransparent,
                darkIcons = navBarDarkIcons,
                navigationBarContrastEnforced = false
            )

        onDispose {}
    }
}