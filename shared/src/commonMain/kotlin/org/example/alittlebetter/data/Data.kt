package org.example.alittlebetter.data

// Phase 3+: SQLDelight-backed repositories (completions, good things).

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import org.example.alittlebetter.core.time.currentLocalDate
import org.example.alittlebetter.db.AppDatabase
import org.example.alittlebetter.domain.GoodThingEntry
import org.example.alittlebetter.domain.GoodThingRepository
import org.example.alittlebetter.domain.StepCompletionRepository

class SqlStepCompletionRepository(private val database: AppDatabase) : StepCompletionRepository {
    override suspend fun recordCompletion(date: LocalDate) {
        withContext(Dispatchers.Default) {
            database.stepCompletionQueries.insertCompletion(date.toString())
        }
    }

    override suspend fun countCompletedDays(): Int {
        return withContext(Dispatchers.Default) {
            database.stepCompletionQueries.countAll().executeAsOne().toInt()
        }
    }
}

class SqlGoodThingRepository(private val database: AppDatabase) : GoodThingRepository {
    override suspend fun addGoodThing(text: String) {
        withContext(Dispatchers.Default) {
            database.goodThingQueries.insertGoodThing(currentLocalDate().toString(), text)
        }
    }

    override suspend fun getAllGoodThings(): List<GoodThingEntry> {
        return withContext(Dispatchers.Default) {
            database.goodThingQueries.selectAll().executeAsList().map { row ->
                GoodThingEntry(date = LocalDate.parse(row.date), text = row.text)
            }
        }
    }
}