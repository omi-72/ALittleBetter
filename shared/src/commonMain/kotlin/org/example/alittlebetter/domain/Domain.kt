package org.example.alittlebetter.domain

import kotlinx.datetime.LocalDate

// Phase 3+: models and use cases, independent of Compose and SQLDelight.

enum class ThoughtCategory(val label: String, val emoji: String) {
    GROWTH("Growth", "🌱"),
    SELF_KINDNESS("Self-kindness", "🤍"),
    HOPE("Hope", "🌅"),
    LETTING_GO("Letting go", "🕊️"),
    COURAGE("Courage", "🔥"),
}

data class Thought(val category: ThoughtCategory, val text: String)

private val thoughts = listOf(
    Thought(ThoughtCategory.GROWTH, "Small progress is still progress."),
    Thought(ThoughtCategory.GROWTH, "Today only asks a little of you."),
    Thought(ThoughtCategory.GROWTH, "Growing doesn't always feel like moving forward."),
    Thought(ThoughtCategory.SELF_KINDNESS, "You are allowed to take things one step at a time."),
    Thought(ThoughtCategory.SELF_KINDNESS, "You don't have to be perfect to be worthy of rest."),
    Thought(ThoughtCategory.SELF_KINDNESS, "Speak to yourself the way you would to someone you love."),
    Thought(ThoughtCategory.HOPE, "A difficult day doesn't mean a difficult life."),
    Thought(ThoughtCategory.HOPE, "Some things don't need an answer tonight."),
    Thought(ThoughtCategory.HOPE, "Tomorrow gets another chance."),
    Thought(ThoughtCategory.LETTING_GO, "You don't have to carry everything today."),
    Thought(ThoughtCategory.LETTING_GO, "Letting go isn't giving up - it's making room for something lighter."),
    Thought(ThoughtCategory.COURAGE, "You can be afraid and still take the next step."),
    Thought(ThoughtCategory.COURAGE, "Courage is quiet more often than it's loud."),
)

fun dailyThoughtFor(dayOfYear: Int): Thought = thoughts[dayOfYear % thoughts.size]

fun thoughtsFor(category: ThoughtCategory): List<Thought> = thoughts.filter { it.category == category }

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