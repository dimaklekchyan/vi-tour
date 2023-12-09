package ru.vi_tour.feature_video.screens.video

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.video.Quality
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import ru.vi_tour.core.rememberPermissionController
import ru.vi_tour.design_system.components.AppScaffold
import ru.vi_tour.design_system.components.TransparentSystemBars
import ru.vi_tour.design_system.dialogs.AppAlertDialog
import ru.vi_tour.design_system.dialogs.LoadingIndicator
import ru.vi_tour.design_system.dialogs.PermissionsRationaleDialog
import ru.vi_tour.design_system.snackbars.AppSnackBarError
import ru.vi_tour.feature_video.biz.VideoRecordStorageOptions
import ru.vi_tour.feature_video.biz.VideoRecorder
import ru.vi_tour.feature_video.ui_components.VideoCamera

@SuppressLint("MissingPermission")
@Composable
internal fun VideoScreenContent(vm: VideoScreenViewModel) {

    val context = LocalContext.current

    val state = vm.collectState()

    TransparentSystemBars(false, false)

    LaunchedEffect(key1 = Unit) {
        vm.obtainEvent(VideoScreenEvent.WantToRecordVideo)
    }

    val permissionController = rememberPermissionController(
        onGranted = { _, permissions ->
            if (permissions.contains(VideoRecorder.PERMISSION)) {
                vm.obtainEvent(VideoScreenEvent.PermissionsGranted)
            }
        },
        onDenied = { _, _ -> },
        onShouldShowRationale = { _, _ ->
            vm.obtainEvent(VideoScreenEvent.ShowPermissionsRationale)
        }
    )

    vm.collectActions { action ->
        when(action) {
            is VideoScreenAction.AskForPermissions -> {
                permissionController.launchPermission(VideoRecorder.PERMISSION)
            }
        }
    }

    if (state.value.showPermissionsRationale) {
        PermissionsRationaleDialog(
            title = "Разрешения не даны",
            description = "Для использования камеры необходимо предоставить разрешения",
            confirmButtonText = "Перейти в настройки",
            dismissButtonText = "Отмена",
            onConfirm = remember(vm) {{
                permissionController.openAppSettings()
                vm.obtainEvent(VideoScreenEvent.HidePermissionsRationale)
            }},
            onDismiss = remember(vm) {{
                vm.obtainEvent(VideoScreenEvent.HidePermissionsRationale)
                (context as Activity).finish()
            }}
        )
    }

    if (state.value.showUploadingDialog) {
        AppAlertDialog(
            title = "Отправить видео?",
            description = "",
            confirmButtonText = "Да",
            dismissButtonText = "Нет",
            onConfirm = remember(vm) {{
                vm.obtainEvent(VideoScreenEvent.ConfirmUploading)
            }},
            onDismiss = remember(vm) {{
                vm.obtainEvent(VideoScreenEvent.DismissUploading)
            }},
            onOutsideClick = {}
        )
    }

    if (state.value.loading) {
        LoadingIndicator()
    }

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.Black
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.value.permissionsGranted) {
                VideoCamera(
                    modifier = Modifier,
                    storageOptions = VideoRecordStorageOptions
                        .getDefault(context)
                        .copy(
                            storage = VideoRecordStorageOptions.Storage.Internal,
                            outputName = "vi_tour_${VideoRecordStorageOptions.date}"
                        ),
                    availableQualities = arrayOf(Quality.HIGHEST),
                    availableLenses = arrayOf(CameraSelector.LENS_FACING_BACK),
                    onSuccess = remember {{ uri ->
                        vm.obtainEvent(VideoScreenEvent.VideoRecorder(uri))
                    }},
                    onError = {
                        Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                    },
                    onConfigChanged = remember {{ config ->
                        vm.obtainEvent(VideoScreenEvent.CameraConfigChanged(config))
                    }},
                )
            }
            if (state.value.sendingError != null) {
                AppSnackBarError(
                    message = state.value.sendingError?.message(),
                    onErrorClick = remember(vm) {{
                        vm.obtainEvent(VideoScreenEvent.ClickOnSendingError)
                    }}
                )
            }
        }
    }
}