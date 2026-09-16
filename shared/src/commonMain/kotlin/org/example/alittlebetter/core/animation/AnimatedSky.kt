package org.example.alittlebetter.core.animation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.example.alittlebetter.core.designsystem.AppColors
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun AnimatedSky(
    modifier: Modifier = Modifier,
    timeOfDay: TimeOfDay = rememberCurrentTimeOfDay(),
    content: @Composable BoxScope.() -> Unit = {},
) {
    val palette = AppColors.paletteFor(timeOfDay)
    val topColor by animateColorAsState(palette.top, animationSpec = tween(3000), label = "skyTop")
    val bottomColor by animateColorAsState(palette.bottom, animationSpec = tween(3000), label = "skyBottom")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(topColor, bottomColor))),
    ) {
        if (timeOfDay == TimeOfDay.NIGHT) {
            TwinklingStars(modifier = Modifier.fillMaxSize())
        }
        DriftingClouds(
            modifier = Modifier.fillMaxSize(),
            tint = if (timeOfDay == TimeOfDay.NIGHT) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.6f),
        )
        if (timeOfDay != TimeOfDay.NIGHT) {
            FlyingBird(modifier = Modifier.fillMaxSize())
        }
        content()
    }
}

private data class CloudSpec(val yFraction: Float, val scale: Float, val durationMillis: Int, val startDelayFraction: Float)

@Composable
private fun DriftingClouds(modifier: Modifier = Modifier, tint: Color) {
    val clouds = remember {
        List(3) {
            CloudSpec(
                yFraction = 0.08f + Random.nextFloat() * 0.3f,
                scale = 0.7f + Random.nextFloat() * 0.8f,
                durationMillis = 35_000 + Random.nextInt(20_000),
                startDelayFraction = Random.nextFloat(),
            )
        }
    }
    Box(modifier) {
        clouds.forEach { cloud ->
            val transition = rememberInfiniteTransition(label = "cloud")
            val progress by transition.animateFloat(
                initialValue = -cloud.startDelayFraction,
                targetValue = 1f - cloud.startDelayFraction,
                animationSpec = infiniteRepeatable(
                    animation = tween(cloud.durationMillis, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "cloudProgress",
            )
            val wrapped = ((progress % 1f) + 1f) % 1f
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cloudWidth = size.width * 0.28f * cloud.scale
                val x = wrapped * (size.width + cloudWidth * 2) - cloudWidth
                val y = size.height * cloud.yFraction
                drawCloud(center = Offset(x, y), width = cloudWidth, color = tint)
            }
        }
    }
}

private fun DrawScope.drawCloud(center: Offset, width: Float, color: Color) {
    val r = width * 0.32f
    drawCircle(color, radius = r, center = Offset(center.x - r * 0.9f, center.y))
    drawCircle(color, radius = r * 1.15f, center = center)
    drawCircle(color, radius = r * 0.85f, center = Offset(center.x + r * 1.1f, center.y + r * 0.1f))
}

private data class StarSpec(val xFraction: Float, val yFraction: Float, val phase: Float, val sizeDp: Float)

@Composable
private fun TwinklingStars(modifier: Modifier = Modifier) {
    val stars = remember {
        List(40) {
            StarSpec(
                xFraction = Random.nextFloat(),
                yFraction = Random.nextFloat() * 0.7f,
                phase = Random.nextFloat() * 6.28f,
                sizeDp = 1f + Random.nextFloat() * 1.8f,
            )
        }
    }
    val transition = rememberInfiniteTransition(label = "stars")
    val clock by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6.2832f,
        animationSpec = infiniteRepeatable(animation = tween(4000, easing = LinearEasing)),
        label = "starClock",
    )
    Canvas(modifier = modifier) {
        stars.forEach { star ->
            val alpha = 0.25f + 0.75f * abs(sin(clock + star.phase))
            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = star.sizeDp.dp.toPx(),
                center = Offset(star.xFraction * size.width, star.yFraction * size.height),
            )
        }
    }
}

@Composable
private fun FlyingBird(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(-0.2f) }
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(8_000L + Random.nextLong(7_000L))
            progress.snapTo(-0.15f)
            progress.animateTo(1.15f, animationSpec = tween(5_000, easing = LinearEasing))
        }
    }
    BoxWithConstraints(modifier = modifier) {
        val x = maxWidth * progress.value
        val y = maxHeight * (0.12f + 0.05f * sin(progress.value * 6.28f))
        BasicText(
            text = "🐦",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.offset(x = x, y = y),
        )
    }
}