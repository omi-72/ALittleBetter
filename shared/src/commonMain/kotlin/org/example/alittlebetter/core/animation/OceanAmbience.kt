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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.PI
import kotlin.math.sin

private data class WaveSpec(
    val yFraction: Float,
    val amplitudeFraction: Float,
    val wavelengthFraction: Float,
    val durationMillis: Int,
    val color: Color,
)

private val oceanWaves = listOf(
    WaveSpec(yFraction = 0.62f, amplitudeFraction = 0.018f, wavelengthFraction = 0.9f, durationMillis = 6_000, color = Color(0xFF4FA5D8).copy(alpha = 0.4f)),
    WaveSpec(yFraction = 0.72f, amplitudeFraction = 0.025f, wavelengthFraction = 1.3f, durationMillis = 8_000, color = Color(0xFF3A82B8).copy(alpha = 0.55f)),
    WaveSpec(yFraction = 0.84f, amplitudeFraction = 0.02f, wavelengthFraction = 0.7f, durationMillis = 5_000, color = Color(0xFF2C6491).copy(alpha = 0.75f)),
)

/** Layered animated waves - a Peace Space ambience scene, no interaction required. */
@Composable
fun OceanAmbience(modifier: Modifier = Modifier) {
    val waves = remember { oceanWaves }
    val phases = waves.map { wave ->
        val transition = rememberInfiniteTransition(label = "oceanWave")
        val phase by transition.animateFloat(
            initialValue = 0f,
            targetValue = (2 * PI).toFloat(),
            animationSpec = infiniteRepeatable(animation = tween(wave.durationMillis, easing = LinearEasing)),
            label = "wavePhase",
        )
        phase
    }
    Canvas(modifier = modifier) {
        waves.forEachIndexed { index, wave -> drawWave(wave, phases[index]) }
    }
}

private fun DrawScope.drawWave(wave: WaveSpec, phase: Float) {
    val baseY = size.height * wave.yFraction
    val amplitude = size.height * wave.amplitudeFraction
    val wavelength = size.width * wave.wavelengthFraction
    val path = Path().apply {
        moveTo(0f, baseY)
        var x = 0f
        while (x <= size.width) {
            val y = baseY + amplitude * sin((x / wavelength) * 2 * PI.toFloat() + phase)
            lineTo(x, y)
            x += 8f
        }
        lineTo(size.width, size.height)
        lineTo(0f, size.height)
        close()
    }
    drawPath(path, color = wave.color)
}
