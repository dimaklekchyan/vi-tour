package ru.vi_tour.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun LifecycleEffect(
    key: Any? = null,
    onCreate: Lifecycle.Event.() -> Unit = {},
    onStart: Lifecycle.Event.() -> Unit = {},
    onResume:  Lifecycle.Event.() -> Unit = {},
    onPause: Lifecycle.Event.() -> Unit = {},
    onStop: Lifecycle.Event.() -> Unit = {},
    onDestroy: Lifecycle.Event.() -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = key) {
        val observer = LifecycleEventObserver { _, event ->
            event.apply {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> onCreate()
                    Lifecycle.Event.ON_START -> onStart()
                    Lifecycle.Event.ON_RESUME -> onResume()
                    Lifecycle.Event.ON_PAUSE -> onPause()
                    Lifecycle.Event.ON_STOP -> onStop()
                    Lifecycle.Event.ON_DESTROY -> onDestroy()
                    Lifecycle.Event.ON_ANY -> {}
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}