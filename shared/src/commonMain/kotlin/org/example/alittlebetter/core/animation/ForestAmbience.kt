package org.example.alittlebetter.core.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private data class TreeSpec(val xFraction: Float, val sizeSp: Float, val swayDurationMillis: Int)
private data class LeafSpec(val xFraction: Float, val startDelayFraction: Float, val driftFraction: Float)

/** Swaying trees with drifting leaves - a Peace Space ambience scene, no interaction required. */
@Composable
fun ForestAmbience(modifier: Modifier = Modifier, treeCount: Int = 5, leafCount: Int = 10) {
    val trees = remember {
        List(treeCount) { index ->
            TreeSpec(
                xFraction = (index + 0.5f) / treeCount,
                sizeSp = 56f + Random.nextFloat() * 24f,
                swayDurationMillis = 3_500 + Random.nextInt(1_500),
            )
        }
    }
    val leaves = remember {
        List(leafCount) {
            LeafSpec(
                xFraction = Random.nextFloat(),
                startDelayFraction = Random.nextFloat(),
                driftFraction = (Random.nextFloat() - 0.5f) * 0.3f,
            )
        }
    }
    val leafTransition = rememberInfiniteTransition(label = "leaves")
    val leafClock by leafTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(6_000, easing = LinearEasing)),
        label = "leafClock",
    )

    BoxWithConstraints(modifier = modifier) {
        trees.forEach { tree ->
            val swayTransition = rememberInfiniteTransition(label = "treeSway")
            val sway by swayTransition.animateFloat(
                initialValue = -4f,
                targetValue = 4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(tree.swayDurationMillis, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "swayAngle",
            )
            BasicText(
                text = "🌲",
                style = TextStyle(fontSize = tree.sizeSp.sp),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .graphicsLayer {
                        translationX = maxWidth.toPx() * tree.xFraction
                        rotationZ = sway
                        transformOrigin = TransformOrigin(0.5f, 1f)
                    },
            )
        }

        leaves.forEach { leaf ->
            val progress = (leafClock + leaf.startDelayFraction) % 1f
            BasicText(
                text = "🍃",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.graphicsLayer {
                    translationX = maxWidth.toPx() * (leaf.xFraction + leaf.driftFraction * progress)
                    translationY = maxHeight.toPx() * progress
                    alpha = 1f - progress
                },
            )
        }
    }
}
