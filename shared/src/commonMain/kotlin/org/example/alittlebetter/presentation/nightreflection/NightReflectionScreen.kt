package org.example.alittlebetter.presentation.nightreflection

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import org.example.alittlebetter.core.animation.TimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors
import org.koin.compose.viewmodel.koinViewModel

/** Always rendered with a forced night sky - the calming atmosphere is the point, not the clock. */
@Composable
fun NightReflectionScreen(onBack: () -> Unit, viewModel: NightReflectionViewModel = koinViewModel()) {
    val ink = AppColors.night.ink
    val state by viewModel.state.collectAsState()

    AnimatedSky(timeOfDay = TimeOfDay.NIGHT) {
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

            AnimatedContent(
                targetState = state.phase,
                transitionSpec = { fadeIn(tween(500)).togetherWith(fadeOut(tween(300))) },
                label = "nightReflection",
            ) { phase ->
                when (phase) {
                    NightReflectionPhase.ANSWERING -> AnsweringContent(
                        ink = ink,
                        state = state,
                        onAnswerChange = viewModel::updateAnswer,
                        onNext = viewModel::next,
                    )
                    NightReflectionPhase.DONE -> DoneContent(ink = ink, onDone = onBack)
                }
            }
        }
    }
}

@Composable
private fun AnsweringContent(
    ink: Color,
    state: NightReflectionUiState,
    onAnswerChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "🌙 Night Reflection",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = ink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Question ${state.questionIndex + 1} of ${state.totalQuestions}",
            fontSize = 13.sp,
            color = ink.copy(alpha = 0.7f),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = state.currentQuestion,
            fontSize = 18.sp,
            color = ink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = state.currentAnswer,
            onValueChange = onAnswerChange,
            placeholder = { Text("Optional...") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNext) { Text(if (state.isLastQuestion) "Finish" else "Next") }
    }
}

@Composable
private fun DoneContent(ink: Color, onDone: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "🌙", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Rest well.",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = ink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onDone) { Text("Done") }
    }
}
