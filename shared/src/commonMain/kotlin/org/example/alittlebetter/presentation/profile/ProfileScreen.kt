package org.example.alittlebetter.presentation.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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

@Composable
fun ProfileScreen(onBack: () -> Unit, viewModel: ProfileViewModel = koinViewModel()) {
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

            Text(text = "Your Journey", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = ink)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${state.growthStage.emoji} You're at the ${state.growthStage.label} stage",
                fontSize = 16.sp,
                color = ink,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(32.dp))

            StatLine(ink = ink, emoji = "🌱", text = statText(state.littleStepsCount, "little step", "little steps") + " taken")

            Spacer(modifier = Modifier.height(16.dp))

            StatLine(ink = ink, emoji = "✨", text = statText(state.goodThingsCount, "good thing", "good things") + " noticed")

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "However far you've come, it's enough.",
                fontSize = 14.sp,
                color = ink.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun statText(count: Int, singular: String, plural: String): String =
    "$count ${if (count == 1) singular else plural}"

@Composable
private fun StatLine(ink: Color, emoji: String, text: String) {
    Text(text = "$emoji $text", fontSize = 17.sp, color = ink, textAlign = TextAlign.Center)
}
