package org.example.alittlebetter.presentation.home

// Phase 2: Home screen (live sky + daily thought + little step card).

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.alittlebetter.core.animation.AnimatedSky
import org.example.alittlebetter.core.animation.TimeOfDay
import org.example.alittlebetter.core.animation.rememberCurrentTimeOfDay
import org.example.alittlebetter.core.designsystem.AppColors
import org.example.alittlebetter.core.time.currentLocalDateTime
import org.example.alittlebetter.domain.dailyQuoteFor

@Composable
fun HomeScreen(
    onLittleStepClick: () -> Unit,
    onGoodThingClick: () -> Unit,
    onGrowthClick: () -> Unit,
    onLetItGoClick: () -> Unit,
    onMemoriesClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    val timeOfDay = rememberCurrentTimeOfDay()
    val ink by animateColorAsState(AppColors.paletteFor(timeOfDay).ink, animationSpec = tween(3000), label = "inkColor")
    val quote = remember { dailyQuoteFor(currentLocalDateTime().date.dayOfYear) }

    AnimatedSky(timeOfDay = timeOfDay) {
        Text(
            text = "👤",
            fontSize = 20.sp,
            color = ink,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .safeContentPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .clickable(onClick = onProfileClick),
        )

        Column(
            modifier = Modifier
                .safeContentPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = greetingFor(timeOfDay),
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = ink,
            )

            Spacer(modifier = Modifier.weight(0.3f))

            Text(
                text = quote,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                color = ink,
            )

            Spacer(modifier = Modifier.weight(1f))

            LittleStepCard(
                title = "🌱 Today's Little Step",
                body = "Take 10 minutes for yourself.",
                modifier = Modifier.clickable(onClick = onLittleStepClick),
            )

            Spacer(modifier = Modifier.weight(0.4f))

            GoodThingTeaser(ink = ink, modifier = Modifier.clickable(onClick = onGoodThingClick))

            Spacer(modifier = Modifier.weight(0.3f))

            GrowthTeaser(ink = ink, modifier = Modifier.clickable(onClick = onGrowthClick))

            Spacer(modifier = Modifier.weight(0.4f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(vertical = 4.dp),
            ) {
                Text(
                    text = "🕊️ Let it go",
                    fontSize = 13.sp,
                    color = ink.copy(alpha = 0.7f),
                    modifier = Modifier.clickable(onClick = onLetItGoClick),
                )
                Text(
                    text = "📅 Memories",
                    fontSize = 13.sp,
                    color = ink.copy(alpha = 0.7f),
                    modifier = Modifier.clickable(onClick = onMemoriesClick),
                )
            }

            Spacer(modifier = Modifier.weight(0.4f))
        }
    }
}

private fun greetingFor(timeOfDay: TimeOfDay): String = when (timeOfDay) {
    TimeOfDay.MORNING -> "Good Morning"
    TimeOfDay.AFTERNOON -> "Good Afternoon"
    TimeOfDay.EVENING -> "Good Evening"
    TimeOfDay.NIGHT -> "Good Night"
}

@Composable
private fun LittleStepCard(title: String, body: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.28f))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.White)
        Text(text = body, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}

@Composable
private fun GoodThingTeaser(ink: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text = "✨ One Good Thing", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ink)
        Text(text = "What made you smile?", fontSize = 13.sp, color = ink.copy(alpha = 0.75f))
    }
}

@Composable
private fun GrowthTeaser(ink: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text = "🌱 My Growth", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ink)
        Text(text = "See how far you've come.", fontSize = 13.sp, color = ink.copy(alpha = 0.75f))
    }
}