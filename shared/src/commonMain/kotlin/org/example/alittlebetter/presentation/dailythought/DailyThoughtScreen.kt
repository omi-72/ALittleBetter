package org.example.alittlebetter.presentation.dailythought

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.alittlebetter.core.animation.AnimatedSky
import org.example.alittlebetter.core.animation.LetterRevealText
import org.example.alittlebetter.core.animation.rememberCurrentTimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors
import org.example.alittlebetter.domain.ThoughtCategory
import org.example.alittlebetter.domain.thoughtsFor

@Composable
fun DailyThoughtScreen(initialCategory: ThoughtCategory, onBack: () -> Unit) {
    val timeOfDay = rememberCurrentTimeOfDay()
    val ink by animateColorAsState(AppColors.paletteFor(timeOfDay).ink, animationSpec = tween(3000), label = "inkColor")

    var category by remember { mutableStateOf(initialCategory) }
    var index by remember(category) { mutableIntStateOf(0) }
    val categoryThoughts = remember(category) { thoughtsFor(category) }
    val thought = categoryThoughts[index % categoryThoughts.size]

    AnimatedSky(timeOfDay = timeOfDay) {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "← Back",
                color = ink,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onBack)
                    .padding(vertical = 8.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThoughtCategory.entries.forEach { entry ->
                    CategoryChip(
                        category = entry,
                        isSelected = entry == category,
                        onClick = {
                            category = entry
                            index = 0
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            LetterRevealText(
                text = thought.text,
                color = ink,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = { index++ }) { Text("Another thought") }
        }
    }
}

@Composable
private fun CategoryChip(category: ThoughtCategory, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color.White.copy(alpha = 0.35f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(text = "${category.emoji} ${category.label}", fontSize = 12.sp)
    }
}
