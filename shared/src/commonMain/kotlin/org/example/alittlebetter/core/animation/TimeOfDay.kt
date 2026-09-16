package org.example.alittlebetter.core.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

enum class TimeOfDay { MORNING, AFTERNOON, EVENING, NIGHT }

fun timeOfDayForHour(hour: Int): TimeOfDay = when (hour) {
    in 5..10 -> TimeOfDay.MORNING
    in 11..16 -> TimeOfDay.AFTERNOON
    in 17..19 -> TimeOfDay.EVENING
    else -> TimeOfDay.NIGHT
}

@OptIn(ExperimentalTime::class)
private fun currentTimeOfDay(): TimeOfDay {
    val hour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
    return timeOfDayForHour(hour)
}

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