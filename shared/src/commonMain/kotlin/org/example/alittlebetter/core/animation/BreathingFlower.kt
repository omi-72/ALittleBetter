package org.example.alittlebetter.core.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp

/**
 * A flower that expands on "inhale" and contracts on "exhale", cycling through
 * seed -> sprout -> bloom stages as it grows. Runs continuously while [isActive];
 * a caller drives its own countdown/timer alongside this.
 */
@Composable
fun BreathingFlower(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    cycleDurationMillis: Int = 8_000,
) {
    val progress: Float = if (isActive) {
        val transition = rememberInfiniteTransition(label = "breathing")
        val value by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(cycleDurationMillis, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "breathingProgress",
        )
        value
    } else {
        0f
    }

    val scale = 0.82f + progress * 0.35f
    val stageEmoji = when {
        progress < 0.34f -> "🌱"
        progress < 0.67f -> "🌿"
        else -> "🌸"
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        BasicText(
            text = stageEmoji,
            style = TextStyle(fontSize = 96.sp),
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        )
    }
}