package ru.vi_tour.design_system.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vi_tour.design_system.buttons.PrimaryButton
import ru.vi_tour.design_system.buttons.SecondaryButton
import ru.vi_tour.design_system.theme.AppTheme

@Composable
fun AppAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onOutsideClick: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        shape = AppTheme.shapes.large,
        backgroundColor = AppTheme.colors.backgroundPrimary,
        title = {
            Text(
                text = title,
                style = AppTheme.typography.h3
            )
        },
        text = {
            Text(
                text = description,
                style = AppTheme.typography.h5,
            )
        },
        onDismissRequest = onOutsideClick,
        confirmButton = {
            PrimaryButton(
                modifier = Modifier.padding(bottom = 5.dp),
                buttonText = confirmButtonText,
                enabled = true,
                onClick = onConfirm
            )
        },
        dismissButton = {
            if (dismissButtonText.isNotEmpty()) {
                SecondaryButton(
                    buttonText = dismissButtonText,
                    enabled = true,
                    onClick = onDismiss
                )
            }
        }
    )
}