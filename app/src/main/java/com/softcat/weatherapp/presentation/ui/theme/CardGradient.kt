package com.softcat.weatherapp.presentation.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class CardGradient(
    val primaryGradient: Brush,
    val secondaryGradient: Brush,
    val shadow: Color
) {
    constructor(
        firstColor: Color,
        secondColor: Color,
        thirdColor: Color,
        fourthColor: Color
    ): this(
        primaryGradient = Brush.linearGradient(listOf(firstColor, secondColor)),
        secondaryGradient = Brush.linearGradient(listOf(thirdColor, fourthColor)),
        shadow = firstColor
    )
}
