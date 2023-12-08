package ru.vi_tour.design_system.dialogs

import androidx.compose.runtime.Composable

@Composable
fun PermissionsRationaleDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AppAlertDialog(
        title = title,
        description = description,
        confirmButtonText = confirmButtonText,
        dismissButtonText = dismissButtonText,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        onOutsideClick = onDismiss
    )
}