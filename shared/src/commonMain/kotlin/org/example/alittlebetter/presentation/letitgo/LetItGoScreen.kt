package org.example.alittlebetter.presentation.letitgo

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.alittlebetter.core.animation.AnimatedSky
import org.example.alittlebetter.core.animation.FloatingParticles
import org.example.alittlebetter.core.animation.rememberCurrentTimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors

private enum class LetItGoPhase { WRITING, RELEASING, RELEASED }

/**
 * Purely an emotional-release interaction - the worry is never persisted anywhere,
 * it just dissolves into [FloatingParticles] and is gone.
 */
@Composable
fun LetItGoScreen(onBack: () -> Unit) {
    val timeOfDay = rememberCurrentTimeOfDay()
    val ink by animateColorAsState(AppColors.paletteFor(timeOfDay).ink, animationSpec = tween(3000), label = "inkColor")
    var phase by remember { mutableStateOf(LetItGoPhase.WRITING) }
    var text by remember { mutableStateOf("") }

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

            AnimatedContent(
                targetState = phase,
                transitionSpec = { fadeIn(tween(500)).togetherWith(fadeOut(tween(300))) },
                label = "letItGo",
            ) { currentPhase ->
                when (currentPhase) {
                    LetItGoPhase.WRITING -> WritingContent(
                        ink = ink,
                        text = text,
                        onTextChange = { text = it },
                        onRelease = { phase = LetItGoPhase.RELEASING },
                    )
                    LetItGoPhase.RELEASING -> ReleasingContent(
                        ink = ink,
                        text = text,
                        onFinished = {
                            text = ""
                            phase = LetItGoPhase.RELEASED
                        },
                    )
                    LetItGoPhase.RELEASED -> ReleasedContent(ink = ink, onDone = onBack)
                }
            }
        }
    }
}

@Composable
private fun WritingContent(ink: Color, text: String, onTextChange: (String) -> Unit, onRelease: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "🕊️ Let It Go",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = ink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Write down what's weighing on you.\nYou don't have to carry it right now.",
            fontSize = 16.sp,
            color = ink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("I'm worried about...") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRelease, enabled = text.isNotBlank()) { Text("Let it go") }
    }
}

@Composable
private fun ReleasingContent(ink: Color, text: String, onFinished: () -> Unit) {
    val textAlpha by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(1_400),
        label = "releasingTextAlpha",
    )
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = ink,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(textAlpha).fillMaxWidth().padding(horizontal = 24.dp),
        )
        FloatingParticles(
            trigger = true,
            color = ink,
            modifier = Modifier.fillMaxSize(),
            onFinished = onFinished,
        )
    }
}

@Composable
private fun ReleasedContent(ink: Color, onDone: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "🕊️", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "That's one less thing to carry.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = ink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onDone) { Text("Done") }
    }
}
