package org.example.alittlebetter.data

// Phase 3+: SQLDelight-backed repositories (completions, good things).

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import org.example.alittlebetter.db.AppDatabase
import org.example.alittlebetter.domain.StepCompletionRepository

class SqlStepCompletionRepository(private val database: AppDatabase) : StepCompletionRepository {
    override suspend fun recordCompletion(date: LocalDate) {
        withContext(Dispatchers.Default) {
            database.stepCompletionQueries.insertCompletion(date.toString())
        }
    }
}