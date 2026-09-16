package org.example.alittlebetter.presentation.memories

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.alittlebetter.core.animation.AnimatedSky
import org.example.alittlebetter.core.animation.rememberCurrentTimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors
import org.koin.compose.viewmodel.koinViewModel

private val weekdayLabels = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")

@Composable
fun MemoriesScreen(onBack: () -> Unit, viewModel: MemoriesViewModel = koinViewModel()) {
    val timeOfDay = rememberCurrentTimeOfDay()
    val ink by animateColorAsState(AppColors.paletteFor(timeOfDay).ink, animationSpec = tween(3000), label = "inkColor")
    val state by viewModel.state.collectAsState()

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

            if (state.isLoading) return@Column

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Memories",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = ink,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "‹",
                    fontSize = 22.sp,
                    color = ink,
                    modifier = Modifier.clickable(onClick = viewModel::showPreviousMonth).padding(12.dp),
                )
                Text(text = state.monthLabel, fontSize = 17.sp, fontWeight = FontWeight.Medium, color = ink)
                Text(
                    text = "›",
                    fontSize = 22.sp,
                    color = ink,
                    modifier = Modifier.clickable(onClick = viewModel::showNextMonth).padding(12.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                weekdayLabels.forEach { label ->
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        color = ink.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(36.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            state.cells.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    week.forEach { cell -> DayCell(cell = cell, ink = ink) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🌱 a little step  ·  ✿ a good thing",
                fontSize = 12.sp,
                color = ink.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun DayCell(cell: CalendarCell, ink: Color) {
    Column(
        modifier = Modifier.size(width = 36.dp, height = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (cell.date != null) {
            Text(text = cell.date.day.toString(), fontSize = 13.sp, color = ink)
            Text(
                text = buildString {
                    if (cell.hasLittleStep) append("🌱")
                    if (cell.hasGoodThing) append("✿")
                },
                fontSize = 9.sp,
            )
        }
    }
}
