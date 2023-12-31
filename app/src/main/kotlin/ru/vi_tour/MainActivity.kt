package ru.vi_tour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import ru.vi_tour.core_navigation.VideoNavSharing
import ru.vi_tour.design_system.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                val splashScreen = rememberScreen(VideoNavSharing.VideoGraph)
                Navigator(screen = splashScreen) {
                    SlideTransition(navigator = it)
                }
            }
        }
    }
}