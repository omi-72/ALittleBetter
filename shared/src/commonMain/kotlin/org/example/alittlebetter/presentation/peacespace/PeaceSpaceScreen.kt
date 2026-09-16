package org.example.alittlebetter.presentation.peacespace

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.alittlebetter.core.animation.AnimatedSky
import org.example.alittlebetter.core.animation.BreathingFlower
import org.example.alittlebetter.core.animation.FireplaceAmbience
import org.example.alittlebetter.core.animation.ForestAmbience
import org.example.alittlebetter.core.animation.OceanAmbience
import org.example.alittlebetter.core.animation.RainAmbience
import org.example.alittlebetter.core.animation.TimeOfDay
import org.example.alittlebetter.core.animation.rememberCurrentTimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors

private enum class PeaceScene(val emoji: String, val label: String) {
    BREATHING("🌸", "Breathing"),
    RAIN("🌧️", "Rain"),
    OCEAN("🌊", "Ocean"),
    FIREPLACE("🔥", "Fireplace"),
    FOREST("🌲", "Forest"),
    NIGHT("🌙", "Night"),
}

/** Ambience scenes with no required interaction - pick one and just be with it. */
@Composable
fun PeaceSpaceScreen(onBack: () -> Unit) {
    var scene by remember { mutableStateOf(PeaceScene.BREATHING) }
    val liveTimeOfDay = rememberCurrentTimeOfDay()
    val timeOfDay = if (scene == PeaceScene.NIGHT) TimeOfDay.NIGHT else liveTimeOfDay
    val ink by animateColorAsState(AppColors.paletteFor(timeOfDay).ink, animationSpec = tween(3000), label = "inkColor")

    AnimatedSky(timeOfDay = timeOfDay) {
        when (scene) {
            PeaceScene.BREATHING -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                BreathingFlower(modifier = Modifier.size(200.dp), isActive = true)
            }
            PeaceScene.RAIN -> RainAmbience(modifier = Modifier.fillMaxSize())
            PeaceScene.OCEAN -> OceanAmbience(modifier = Modifier.fillMaxSize())
            PeaceScene.FIREPLACE -> FireplaceAmbience(modifier = Modifier.fillMaxSize())
            PeaceScene.FOREST -> ForestAmbience(modifier = Modifier.fillMaxSize())
            PeaceScene.NIGHT -> Unit
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text(
                text = "← Back",
                color = ink,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onBack)
                    .padding(vertical = 8.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                PeaceScene.entries.forEach { entry ->
                    SceneChip(scene = entry, isSelected = entry == scene, onClick = { scene = entry })
                }
            }
        }
    }
}

@Composable
private fun SceneChip(scene: PeaceScene, isSelected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color.White.copy(alpha = 0.35f) else Color.Transparent)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = scene.emoji, fontSize = 20.sp)
        }
    }
}
