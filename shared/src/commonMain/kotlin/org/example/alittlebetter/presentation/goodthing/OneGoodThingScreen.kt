package org.example.alittlebetter.presentation.goodthing

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import org.example.alittlebetter.core.animation.AnimatedSky
import org.example.alittlebetter.core.animation.rememberCurrentTimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OneGoodThingScreen(
    onBack: () -> Unit,
    onViewGarden: () -> Unit,
    viewModel: OneGoodThingViewModel = koinViewModel(),
) {
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
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "← Back",
                    color = ink,
                    modifier = Modifier.clickable(onClick = onBack).padding(vertical = 8.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "My Garden",
                    color = ink,
                    modifier = Modifier.clickable(onClick = onViewGarden).padding(vertical = 8.dp),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(
                targetState = state.phase,
                transitionSpec = {
                    (fadeIn(tween(500)) + scaleIn(initialScale = 0.85f, animationSpec = tween(500)))
                        .togetherWith(fadeOut(tween(200)))
                },
                label = "oneGoodThing",
            ) { phase ->
                when (phase) {
                    OneGoodThingPhase.WRITING -> WritingContent(
                        ink = ink,
                        text = state.text,
                        onTextChange = viewModel::updateText,
                        onSave = viewModel::save,
                    )
                    OneGoodThingPhase.SAVED -> SavedContent(
                        ink = ink,
                        flowerIndex = state.savedFlowerIndex,
                        onDone = onBack,
                        onViewGarden = onViewGarden,
                    )
                }
            }
        }
    }
}

@Composable
private fun WritingContent(
    ink: Color,
    text: String,
    onTextChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "✨ One Good Thing",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = ink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "What made you smile today?",
            fontSize = 16.sp,
            color = ink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("I had coffee with a friend...") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSave, enabled = text.isNotBlank()) { Text("Save ✨") }
    }
}

@Composable
private fun SavedContent(ink: Color, flowerIndex: Int, onDone: () -> Unit, onViewGarden: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = flowerForIndex(flowerIndex), fontSize = 72.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Added to your garden.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = ink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onViewGarden) { Text("View Garden") }
            Button(onClick = onDone) { Text("Done") }
        }
    }
}