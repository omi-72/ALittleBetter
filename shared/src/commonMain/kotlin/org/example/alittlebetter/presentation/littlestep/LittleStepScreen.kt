package org.example.alittlebetter.presentation.littlestep

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
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
import org.example.alittlebetter.core.animation.BreathingFlower
import org.example.alittlebetter.core.animation.rememberCurrentTimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LittleStepScreen(onBack: () -> Unit, viewModel: LittleStepViewModel = koinViewModel()) {
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

            Spacer(modifier = Modifier.height(32.dp))

            when (state.phase) {
                LittleStepPhase.IDLE -> IdleContent(ink = ink, onStart = viewModel::start)
                LittleStepPhase.RUNNING -> RunningContent(ink = ink, secondsRemaining = state.secondsRemaining)
                LittleStepPhase.FINISHED -> FinishedContent(ink = ink, onDone = onBack)
            }
        }
    }
}

@Composable
private fun IdleContent(ink: Color, onStart: () -> Unit) {
    Text(
        text = "🌱 Today's Little Step",
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = ink,
        textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Give yourself 10 quiet minutes.\n\nPut your phone away.\nSit somewhere comfortable.\nTake a breath.\n\nYou don't need to accomplish anything.",
        fontSize = 16.sp,
        color = ink,
        textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(32.dp))
    BreathingFlower(isActive = false)
    Spacer(modifier = Modifier.height(32.dp))
    Button(onClick = onStart) { Text("Start") }
}

@Composable
private fun RunningContent(ink: Color, secondsRemaining: Int) {
    BreathingFlower(isActive = true)
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = formatCountdown(secondsRemaining),
        fontSize = 32.sp,
        fontWeight = FontWeight.Light,
        color = ink,
    )
}

@Composable
private fun FinishedContent(ink: Color, onDone: () -> Unit) {
    BreathingFlower(isActive = false)
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = "✨ You gave yourself 10 minutes.\n\nThat's a little better.",
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = ink,
        textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(32.dp))
    Button(onClick = onDone) { Text("Done") }
}

private fun formatCountdown(secondsRemaining: Int): String {
    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}