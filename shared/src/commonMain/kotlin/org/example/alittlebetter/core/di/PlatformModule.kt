package org.example.alittlebetter.core.di

import org.koin.core.module.Module

/** Provides the platform's [app.cash.sqldelight.db.SqlDriver] - implemented per target. */
expect val platformModule: Module