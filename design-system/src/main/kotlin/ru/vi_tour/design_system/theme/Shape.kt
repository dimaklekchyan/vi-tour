package ru.vi_tour.design_system.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

data class Shapes(
    val small: CornerBasedShape,
    val medium: CornerBasedShape,
    val large: CornerBasedShape,
    val rounded: CornerBasedShape
)

val shapes = Shapes(
    small = RoundedCornerShape(7.dp),
    medium = RoundedCornerShape(15.dp),
    large = RoundedCornerShape(20.dp),
    rounded = RoundedCornerShape(50)
)