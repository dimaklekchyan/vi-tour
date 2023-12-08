package ru.vi_tour.design_system.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalColorsProvider = staticCompositionLocalOf<Colors> {
    error("No colors provided")
}

val LocalShapesProvider = staticCompositionLocalOf<Shapes> {
    error("No shapes provided")
}

val LocalTypographyProvider = staticCompositionLocalOf<Typography> {
    error("No typography provided")
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColorsProvider provides colors,
        LocalShapesProvider provides shapes,
        LocalTypographyProvider provides typography,
        content = content
    )
}

object AppTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalColorsProvider.current
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapesProvider.current
    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypographyProvider.current
}
