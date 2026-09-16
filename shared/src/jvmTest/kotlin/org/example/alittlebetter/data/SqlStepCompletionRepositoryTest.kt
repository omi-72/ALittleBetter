package org.example.alittlebetter.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.example.alittlebetter.db.AppDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class SqlStepCompletionRepositoryTest {

    @Test
    fun recordCompletion_insertsRow() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val database = AppDatabase(driver)
        val repository = SqlStepCompletionRepository(database)

        runBlocking { repository.recordCompletion(LocalDate(2026, 9, 16)) }

        val rows = database.stepCompletionQueries.selectAll().executeAsList()
        assertEquals(listOf("2026-09-16"), rows)
    }

    @Test
    fun recordCompletion_isIdempotentForSameDate() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val database = AppDatabase(driver)
        val repository = SqlStepCompletionRepository(database)
        val date = LocalDate(2026, 9, 16)

        runBlocking {
            repository.recordCompletion(date)
            repository.recordCompletion(date)
        }

        assertEquals(1, database.stepCompletionQueries.selectAll().executeAsList().size)
    }

    @Test
    fun countCompletedDays_countsDistinctDates() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val database = AppDatabase(driver)
        val repository = SqlStepCompletionRepository(database)

        runBlocking {
            repository.recordCompletion(LocalDate(2026, 9, 14))
            repository.recordCompletion(LocalDate(2026, 9, 15))
            repository.recordCompletion(LocalDate(2026, 9, 15))
        }

        val count = runBlocking { repository.countCompletedDays() }
        assertEquals(2, count)
    }

    @Test
    fun getCompletedDates_returnsParsedDates() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val database = AppDatabase(driver)
        val repository = SqlStepCompletionRepository(database)

        runBlocking {
            repository.recordCompletion(LocalDate(2026, 9, 14))
            repository.recordCompletion(LocalDate(2026, 9, 16))
        }

        val dates = runBlocking { repository.getCompletedDates() }
        assertEquals(listOf(LocalDate(2026, 9, 14), LocalDate(2026, 9, 16)), dates)
    }
}