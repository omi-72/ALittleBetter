package org.example.alittlebetter.core.designsystem

import androidx.compose.ui.graphics.Color
import org.example.alittlebetter.core.animation.TimeOfDay

data class SkyPalette(val top: Color, val bottom: Color, val ink: Color)

object AppColors {
    val morning = SkyPalette(top = Color(0xFFFFE8CC), bottom = Color(0xFFBEE3F8), ink = Color(0xFF3B3B58))
    val afternoon = SkyPalette(top = Color(0xFF8EC9F0), bottom = Color(0xFFE3F6FD), ink = Color(0xFF23324A))
    val evening = SkyPalette(top = Color(0xFFFF9A76), bottom = Color(0xFF6C5CE7), ink = Color(0xFFFDF3EC))
    val night = SkyPalette(top = Color(0xFF0B1233), bottom = Color(0xFF1B2A55), ink = Color(0xFFEDEFF7))

    fun paletteFor(timeOfDay: TimeOfDay): SkyPalette = when (timeOfDay) {
        TimeOfDay.MORNING -> morning
        TimeOfDay.AFTERNOON -> afternoon
        TimeOfDay.EVENING -> evening
        TimeOfDay.NIGHT -> night
    }
}