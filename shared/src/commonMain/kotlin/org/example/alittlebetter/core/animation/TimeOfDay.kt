package org.example.alittlebetter.core.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.example.alittlebetter.core.time.currentLocalDateTime

enum class TimeOfDay { MORNING, AFTERNOON, EVENING, NIGHT }

fun timeOfDayForHour(hour: Int): TimeOfDay = when (hour) {
    in 5..10 -> TimeOfDay.MORNING
    in 11..16 -> TimeOfDay.AFTERNOON
    in 17..19 -> TimeOfDay.EVENING
    else -> TimeOfDay.NIGHT
}

private fun currentTimeOfDay(): TimeOfDay = timeOfDayForHour(currentLocalDateTime().hour)

@Composable
fun rememberCurrentTimeOfDay(pollIntervalMillis: Long = 60_000L): TimeOfDay {
    var timeOfDay by remember { mutableStateOf(currentTimeOfDay()) }
    LaunchedEffect(Unit) {
        while (isActive) {
            timeOfDay = currentTimeOfDay()
            delay(pollIntervalMillis)
        }
    }
    return timeOfDay
}