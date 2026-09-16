package org.example.alittlebetter.core.animation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Temporary QA harness for the Phase 1 animation primitives - lets us see
 * AnimatedSky/BreathingFlower/FloatingParticles/GrowthTree working together
 * before any real screen exists. Phase 2 replaces this with the real Home screen.
 */
@Composable
fun AnimationGallery() {
    var timeOverride by remember { mutableStateOf<TimeOfDay?>(null) }
    var daysCompleted by remember { mutableStateOf(0) }
    var letGoTrigger by remember { mutableStateOf(false) }

    AnimatedSky(timeOfDay = timeOverride ?: rememberCurrentTimeOfDay()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TimeOfDay.entries.forEach { tod ->
                    Button(onClick = { timeOverride = tod }) { Text(tod.name.take(3)) }
                }
                Button(onClick = { timeOverride = null }) { Text("Live") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BreathingFlower(modifier = Modifier.size(160.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { daysCompleted = (daysCompleted - 1).coerceAtLeast(0) }) { Text("-") }
                Text("$daysCompleted days")
                Button(onClick = { daysCompleted++ }) { Text("+") }
            }
            GrowthTree(daysCompleted = daysCompleted, modifier = Modifier.size(120.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { letGoTrigger = true }) { Text("Let It Go 🕊️") }
        }

        FloatingParticles(
            trigger = letGoTrigger,
            modifier = Modifier.fillMaxSize(),
            onFinished = { letGoTrigger = false },
        )
    }
}