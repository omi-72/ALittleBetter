package org.example.alittlebetter.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.example.alittlebetter.db.AppDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class SqlNightReflectionRepositoryTest {

    @Test
    fun saveAnswer_insertsRow() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val database = AppDatabase(driver)
        val repository = SqlNightReflectionRepository(database)

        runBlocking {
            repository.saveAnswer(LocalDate(2026, 9, 16), "What went well today?", "A quiet walk.")
        }

        val rows = database.nightReflectionQueries.selectForDate("2026-09-16").executeAsList()
        assertEquals(1, rows.size)
        assertEquals("A quiet walk.", rows.first().answer)
    }
}
