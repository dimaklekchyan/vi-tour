package ru.vi_tour.design_system.snackbars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vi_tour.design_system.theme.AppTheme
import ru.vi_tour.design_system.theme.colorBlack
import ru.vi_tour.design_system.theme.colorRed

@Composable
fun BoxScope.AppSnackBarError(
    modifier: Modifier = Modifier,
    message: String?,
    visible: Boolean = !message.isNullOrEmpty(),
    onErrorClick: () -> Unit
) {
    AppSnackBar(
        modifier = modifier,
        message = message,
        visible = visible,
        backgroundColor = colorRed,
        contentColor = colorBlack,
        onClick = onErrorClick
    )
}

@Composable
internal fun BoxScope.AppSnackBar(
    modifier: Modifier = Modifier,
    message: String?,
    visible: Boolean,
    clickable: Boolean = true,
    backgroundColor: Color = AppTheme.colors.primary,
    contentColor: Color = AppTheme.colors.onPrimary,
    icon: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    LaunchedEffect(key1 = message) {
        if(!message.isNullOrEmpty()) text = message
    }

    AnimatedVisibility(
        modifier = modifier
            .align(Alignment.BottomStart)
            .navigationBarsPadding(),
        visible = visible,
        enter = expandHorizontally(),
        exit = shrinkHorizontally()
    ) {
        Snackbar(
            modifier = Modifier
                .padding(20.dp)
                .clip(AppTheme.shapes.large)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple(),
                    onClick = onClick,
                    enabled = clickable
                ),
            shape = AppTheme.shapes.large,
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()
                Text(
                    text = text,
                    style = AppTheme.typography.h5,
                    color = contentColor,
                    modifier = Modifier
                        .padding(10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}