package ru.vi_tour.feature_video.screens.video

import android.annotation.SuppressLint
import android.widget.Toast
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
import ru.vi_tour.design_system.dialogs.PermissionsRationaleDialog
import ru.vi_tour.feature_video.biz.VideoRecordStorageOptions
import ru.vi_tour.feature_video.biz.VideoRecorder
import ru.vi_tour.feature_video.biz.VideoStorage
import ru.vi_tour.feature_video.ui_components.VideoCamera

@SuppressLint("MissingPermission")
@Composable
internal fun VideoScreenContent(vm: VideoScreenViewModel) {

    val state = vm.collectState()

    TransparentSystemBars(false, false)

    LaunchedEffect(key1 = Unit) {
        vm.obtainEvent(VideoScreenEvent.WantToRecordVideo)
    }

    val permissionController = rememberPermissionController(
        onGranted = { _, permissions ->
            if (permissions.containsAll((VideoRecorder.PERMISSIONS + VideoStorage.PERMISSION).toList())) {
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
                permissionController.launchPermission(*(VideoRecorder.PERMISSIONS + VideoStorage.PERMISSION))
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
            onOutsideClick = remember(vm) {{
                vm.obtainEvent(VideoScreenEvent.DismissUploading)
            }}
        )
    }

    val context = LocalContext.current

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.Black
    ) {
        if (state.value.permissionsGranted) {
            VideoCamera(
                modifier = Modifier,
                storageOptions = VideoRecordStorageOptions
                    .getDefault(context)
                    .copy(storage = VideoRecordStorageOptions.Storage.External),
                onSuccess = remember {{ uri ->
                    VideoStorage.getVideoByUri(uri, context)?.let { video ->
                        vm.obtainEvent(VideoScreenEvent.VideoRecorder(video))
                    }
                }},
                onError = {
                    Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}