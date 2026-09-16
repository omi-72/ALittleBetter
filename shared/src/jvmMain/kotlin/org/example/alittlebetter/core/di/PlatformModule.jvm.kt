package org.example.alittlebetter.core.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.example.alittlebetter.db.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val platformModule: Module = module {
    single<SqlDriver> {
        val dbFile = File(System.getProperty("user.home"), ".alittlebetter/alittlebetter.db")
        dbFile.parentFile?.mkdirs()
        val alreadyExists = dbFile.exists()
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
        if (!alreadyExists) {
            AppDatabase.Schema.create(driver)
        }
        driver
    }
}