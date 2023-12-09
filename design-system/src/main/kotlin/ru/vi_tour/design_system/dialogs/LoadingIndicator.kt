package ru.vi_tour.design_system.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vi_tour.design_system.theme.AppTheme

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    AppDialog(
        modifier = modifier,
        onOutsideClick = {}
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(20.dp).size(50.dp),
            color = AppTheme.colors.primary
        )
    }
}