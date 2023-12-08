package ru.vi_tour.design_system.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import ru.vi_tour.design_system.theme.AppTheme

@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    onOutsideClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onOutsideClick,
        content = {
            Box(
                modifier = modifier
                    .clip(AppTheme.shapes.medium)
                    .background(AppTheme.colors.surfacePrimary)
            ) {
                content()
            }
        }
    )
}