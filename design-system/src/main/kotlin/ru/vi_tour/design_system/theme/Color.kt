package ru.vi_tour.design_system.theme

import androidx.compose.ui.graphics.Color

val colorWhite = Color(0xFFFFFFFF)
val colorLightGray = Color(0xFFF2F2F9)
val colorGray = Color(0xFFE4E4EF)
val colorDarkGray = Color(0xFFC9D5DE)
val colorLightBlue = Color(0xFF5F5FFF)
val colorBlue = Color(0xFF3942F0)
val colorDarkBlue = Color(0xFF030047)
val colorRed = Color(0xFFD0021B)
val colorGreen = Color(0xFF68BA49)
val colorYellow = Color(0xFFF4D037)
val colorTransparent = Color(0x00FFFFFF)
val colorBlack = Color(0xFF000000)

data class Colors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,

    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val surfacePrimary: Color,
    val surfaceSecondary: Color,

    val mainFontColor: Color,
    val hint: Color,
    val indicationGreen: Color,
    val indicationYellow: Color,
    val indicationRed: Color,
    val transparent: Color,
    val border: Color,
    val disabled: Color
)

val colors = Colors(
    primary = colorLightBlue,
    onPrimary = colorWhite,
    secondary = colorWhite,
    onSecondary = colorLightBlue,

    backgroundPrimary = colorWhite,
    backgroundSecondary = colorLightGray,
    surfacePrimary = colorLightGray,
    surfaceSecondary = colorWhite,

    mainFontColor = colorDarkBlue,
    hint = colorLightGray,
    indicationGreen = colorGreen,
    indicationYellow = colorYellow,
    indicationRed = colorRed,
    transparent = colorTransparent,
    border = colorLightGray,
    disabled = colorGray
)