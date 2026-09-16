package org.example.alittlebetter.presentation.growth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.alittlebetter.core.animation.AnimatedSky
import org.example.alittlebetter.core.animation.GrowthTree
import org.example.alittlebetter.core.animation.growthStageFor
import org.example.alittlebetter.core.animation.nextGrowthStage
import org.example.alittlebetter.core.animation.rememberCurrentTimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GrowthScreen(onBack: () -> Unit, viewModel: GrowthViewModel = koinViewModel()) {
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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "My Growth",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = ink,
            )

            Spacer(modifier = Modifier.height(24.dp))

            GrowthTree(daysCompleted = state.daysCompleted, modifier = Modifier.size(140.dp))

            Spacer(modifier = Modifier.height(24.dp))

            val stage = growthStageFor(state.daysCompleted)
            Text(
                text = "${state.daysCompleted} ${if (state.daysCompleted == 1) "day" else "days"} of little steps",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = ink,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stage.message,
                fontSize = 15.sp,
                color = ink.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
            )

            val next = nextGrowthStage(state.daysCompleted)
            if (next != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${next.minDays - state.daysCompleted} more to reach ${next.label} ${next.emoji}",
                    fontSize = 13.sp,
                    color = ink.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
