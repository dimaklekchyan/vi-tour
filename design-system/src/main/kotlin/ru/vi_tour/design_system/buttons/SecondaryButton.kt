package ru.vi_tour.design_system.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vi_tour.design_system.theme.AppTheme

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonText: String = "Secondary Text Button",
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = null,
        shape = AppTheme.shapes.small.copy(CornerSize(50)),
        border = null,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.secondary,
            contentColor = AppTheme.colors.onSecondary,
            disabledBackgroundColor = AppTheme.colors.secondary,
            disabledContentColor = AppTheme.colors.disabled
        ),
        contentPadding = PaddingValues(13.dp),
        content = {
            Text(text = buttonText, style = AppTheme.typography.h4.copy(color = AppTheme.colors.onSecondary), textAlign = TextAlign.Center)
        }
    )
}