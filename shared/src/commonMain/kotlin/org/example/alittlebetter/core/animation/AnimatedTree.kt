package org.example.alittlebetter.core.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

enum class GrowthStage(val emoji: String, val minDays: Int, val label: String, val message: String) {
    SEED("🌱", 0, "Seed", "Every journey starts with a single seed."),
    SPROUT("🌿", 7, "Sprout", "You're growing, little by little."),
    BLOSSOM("🌸", 14, "Blossom", "Look how far you've come."),
    TREE("🌳", 30, "Tree", "You've grown into something strong."),
    RADIANT_TREE("🌳✨", 60, "Radiant Tree", "Your growth is starting to radiate outward."),
    HOME_TREE("🌳🏡", 100, "Home Tree", "This tree is home now."),
}

fun growthStageFor(daysCompleted: Int): GrowthStage =
    GrowthStage.entries.lastOrNull { daysCompleted >= it.minDays } ?: GrowthStage.SEED

/** The next stage still ahead of [daysCompleted], or null once every stage has been reached. */
fun nextGrowthStage(daysCompleted: Int): GrowthStage? =
    GrowthStage.entries.firstOrNull { daysCompleted < it.minDays }

/** Renders the growth-stage emoji for [daysCompleted], animating in when the stage changes. */
@Composable
fun GrowthTree(daysCompleted: Int, modifier: Modifier = Modifier) {
    val stage = growthStageFor(daysCompleted)
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        AnimatedContent(
            targetState = stage,
            transitionSpec = {
                (fadeIn(tween(600)) + scaleIn(initialScale = 0.8f, animationSpec = tween(600)))
                    .togetherWith(fadeOut(tween(300)))
            },
            label = "growthTree",
        ) { currentStage ->
            BasicText(text = currentStage.emoji, style = TextStyle(fontSize = 72.sp))
        }
    }
}