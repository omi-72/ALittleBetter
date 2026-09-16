package org.example.alittlebetter.core.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

private data class ParticleSpec(
    val startXFraction: Float,
    val driftXFraction: Float,
    val sizeDp: Float,
    val delayFraction: Float,
)

/**
 * Dissolves whatever is above it into particles that drift upward and fade out,
 * for the "Let It Go" release interaction. Purely visual - callers decide whether
 * to persist anything before setting [trigger] to true.
 */
@Composable
fun FloatingParticles(
    trigger: Boolean,
    modifier: Modifier = Modifier,
    particleCount: Int = 24,
    color: Color = Color.White,
    onFinished: () -> Unit = {},
) {
    val progress = remember { Animatable(0f) }
    val particles = remember {
        List(particleCount) {
            ParticleSpec(
                startXFraction = Random.nextFloat(),
                driftXFraction = (Random.nextFloat() - 0.5f) * 0.4f,
                sizeDp = 2f + Random.nextFloat() * 4f,
                delayFraction = Random.nextFloat() * 0.3f,
            )
        }
    }

    LaunchedEffect(trigger) {
        if (trigger) {
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = tween(1_800, easing = LinearOutSlowInEasing))
            onFinished()
        }
    }

    if (trigger || progress.value > 0f) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height
            particles.forEach { particle ->
                val local = ((progress.value - particle.delayFraction) / (1f - particle.delayFraction)).coerceIn(0f, 1f)
                if (local > 0f) {
                    val x = (particle.startXFraction + particle.driftXFraction * local) * w
                    val y = h * (1f - local)
                    val alpha = 1f - local
                    drawCircle(
                        color = color.copy(alpha = alpha),
                        radius = particle.sizeDp.dp.toPx(),
                        center = Offset(x, y),
                    )
                }
            }
        }
    }
}