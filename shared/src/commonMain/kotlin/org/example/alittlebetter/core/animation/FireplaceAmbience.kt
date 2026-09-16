package org.example.alittlebetter.core.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private data class EmberSpec(val xFraction: Float, val startDelayFraction: Float, val sizeDp: Float)

/** A flickering flame with rising embers - a Peace Space ambience scene, no interaction required. */
@Composable
fun FireplaceAmbience(modifier: Modifier = Modifier, emberCount: Int = 16) {
    val flicker = rememberInfiniteTransition(label = "flameFlicker")
    val scale by flicker.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(animation = tween(260, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "flameScale",
    )

    val embers = remember {
        List(emberCount) {
            EmberSpec(
                xFraction = 0.35f + Random.nextFloat() * 0.3f,
                startDelayFraction = Random.nextFloat(),
                sizeDp = 2f + Random.nextFloat() * 3f,
            )
        }
    }
    val emberTransition = rememberInfiniteTransition(label = "embers")
    val emberClock by emberTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(3_000, easing = LinearEasing)),
        label = "emberClock",
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            embers.forEach { ember ->
                val progress = (emberClock + ember.startDelayFraction) % 1f
                val x = ember.xFraction * size.width
                val y = size.height * (0.78f - progress * 0.6f)
                drawCircle(
                    color = Color(0xFFFFB74D).copy(alpha = (1f - progress) * 0.8f),
                    radius = ember.sizeDp.dp.toPx(),
                    center = Offset(x, y),
                )
            }
        }
        BasicText(
            text = "🔥",
            style = TextStyle(fontSize = 88.sp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
        )
    }
}
