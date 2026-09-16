package org.example.alittlebetter.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import org.example.alittlebetter.db.AppDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class SqlGoodThingRepositoryTest {

    @Test
    fun addGoodThing_persistsInInsertionOrder() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val database = AppDatabase(driver)
        val repository = SqlGoodThingRepository(database)

        runBlocking {
            repository.addGoodThing("I had coffee with a friend.")
            repository.addGoodThing("Finished something I've been working on.")
        }

        val entries = runBlocking { repository.getAllGoodThings() }
        assertEquals(
            listOf("I had coffee with a friend.", "Finished something I've been working on."),
            entries.map { it.text },
        )
    }
}