package org.example.alittlebetter.domain

import kotlinx.datetime.LocalDate

// Phase 3+: models and use cases, independent of Compose and SQLDelight.

private val dailyQuotes = listOf(
    "You are allowed to take things one step at a time.",
    "Small progress is still progress.",
    "You don't have to be perfect to be worthy of rest.",
    "A difficult day doesn't mean a difficult life.",
    "Some things don't need an answer tonight.",
    "You can be afraid and still take the next step.",
    "Today only asks a little of you.",
)

fun dailyQuoteFor(dayOfYear: Int): String = dailyQuotes[dayOfYear % dailyQuotes.size]

interface StepCompletionRepository {
    suspend fun recordCompletion(date: LocalDate)
}