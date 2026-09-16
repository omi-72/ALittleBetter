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
    suspend fun countCompletedDays(): Int
    suspend fun getCompletedDates(): List<LocalDate>
}

/** Named "Entry" (not "GoodThing") to avoid colliding with SQLDelight's generated row class of that name. */
data class GoodThingEntry(val date: LocalDate, val text: String)

interface GoodThingRepository {
    suspend fun addGoodThing(text: String)
    suspend fun getAllGoodThings(): List<GoodThingEntry>
}

val nightReflectionQuestions = listOf(
    "What's one thing that went well today?",
    "What's something you're grateful for right now?",
    "Is there anything you'd like to let go of before you sleep?",
)

interface NightReflectionRepository {
    suspend fun saveAnswer(date: LocalDate, question: String, answer: String)
}