package org.example.alittlebetter.core.di

import app.cash.sqldelight.db.SqlDriver
import org.example.alittlebetter.data.SqlGoodThingRepository
import org.example.alittlebetter.data.SqlStepCompletionRepository
import org.example.alittlebetter.db.AppDatabase
import org.example.alittlebetter.domain.GoodThingRepository
import org.example.alittlebetter.domain.StepCompletionRepository
import org.example.alittlebetter.presentation.goodthing.OneGoodThingViewModel
import org.example.alittlebetter.presentation.growth.GrowthViewModel
import org.example.alittlebetter.presentation.littlestep.LittleStepViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase(get<SqlDriver>()) }
    single<StepCompletionRepository> { SqlStepCompletionRepository(get()) }
    single<GoodThingRepository> { SqlGoodThingRepository(get()) }
    viewModel { LittleStepViewModel(get()) }
    viewModel { OneGoodThingViewModel(get()) }
    viewModel { GrowthViewModel(get()) }
}

fun initKoin(config: KoinAppDeclaration = {}) {
    startKoin {
        config()
        modules(platformModule, appModule)
    }
}