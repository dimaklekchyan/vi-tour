package ru.vi_tour.design_system.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val defaultFontStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontStyle = FontStyle.Normal,
    color = colorDarkBlue
)

val typography = Typography(
    h1 = defaultFontStyle.copy(fontSize = 24.sp, fontWeight = FontWeight.Medium),
    h2 = defaultFontStyle.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
    h3 = defaultFontStyle.copy(fontSize = 18.sp, fontWeight = FontWeight.Normal),
    h4 = defaultFontStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium),
    h5 = defaultFontStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.Normal),

    body1 = defaultFontStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    body2 = defaultFontStyle.copy(fontSize = 13.sp, fontWeight = FontWeight.Normal),

    subtitle1 = defaultFontStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
    subtitle2 = defaultFontStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal),

    caption = defaultFontStyle.copy(fontSize = 10.sp, fontWeight = FontWeight.Normal)
)
