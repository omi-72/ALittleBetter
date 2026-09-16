package org.example.alittlebetter.presentation.goodthing

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.alittlebetter.core.animation.AnimatedSky
import org.example.alittlebetter.core.animation.rememberCurrentTimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors
import org.example.alittlebetter.domain.GoodThingEntry
import org.example.alittlebetter.domain.GoodThingRepository
import org.koin.compose.koinInject

@Composable
fun MyGardenScreen(onBack: () -> Unit, repository: GoodThingRepository = koinInject()) {
    val timeOfDay = rememberCurrentTimeOfDay()
    val ink by animateColorAsState(AppColors.paletteFor(timeOfDay).ink, animationSpec = tween(3000), label = "inkColor")
    var entries by remember { mutableStateOf<List<GoodThingEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        entries = repository.getAllGoodThings()
    }

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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (entries.isEmpty()) {
                    "Your garden is waiting for its first flower."
                } else {
                    "${entries.size} little good ${if (entries.size == 1) "thing" else "things"} you've collected. 🤍"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = ink,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            FlowRow(modifier = Modifier.fillMaxWidth()) {
                entries.forEachIndexed { index, _ ->
                    Text(text = flowerForIndex(index), fontSize = 32.sp, modifier = Modifier.padding(6.dp))
                }
            }
        }
    }
}