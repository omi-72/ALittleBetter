package org.example.alittlebetter.core.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlin.random.Random

private data class RaindropSpec(
    val xFraction: Float,
    val lengthFraction: Float,
    val speedFactor: Float,
    val startDelayFraction: Float,
)

/** Continuous falling rain streaks - a Peace Space ambience scene, no interaction required. */
@Composable
fun RainAmbience(modifier: Modifier = Modifier, dropCount: Int = 60, color: Color = Color.White.copy(alpha = 0.5f)) {
    val drops = remember {
        List(dropCount) {
            RaindropSpec(
                xFraction = Random.nextFloat(),
                lengthFraction = 0.03f + Random.nextFloat() * 0.05f,
                speedFactor = 0.7f + Random.nextFloat() * 0.6f,
                startDelayFraction = Random.nextFloat(),
            )
        }
    }
    val transition = rememberInfiniteTransition(label = "rain")
    val clock by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(900, easing = LinearEasing)),
        label = "rainClock",
    )
    Canvas(modifier = modifier) {
        drops.forEach { drop ->
            val progress = (clock * drop.speedFactor + drop.startDelayFraction) % 1f
            val x = drop.xFraction * size.width
            val y = progress * (size.height * 1.2f) - size.height * 0.1f
            drawLine(
                color = color,
                start = Offset(x, y),
                end = Offset(x, y + size.height * drop.lengthFraction),
                strokeWidth = 2f,
                cap = StrokeCap.Round,
            )
        }
    }
}
