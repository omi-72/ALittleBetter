package org.example.alittlebetter.core.di

import app.cash.sqldelight.db.SqlDriver
import org.example.alittlebetter.data.SqlGoodThingRepository
import org.example.alittlebetter.data.SqlNightReflectionRepository
import org.example.alittlebetter.data.SqlStepCompletionRepository
import org.example.alittlebetter.db.AppDatabase
import org.example.alittlebetter.domain.GoodThingRepository
import org.example.alittlebetter.domain.NightReflectionRepository
import org.example.alittlebetter.domain.StepCompletionRepository
import org.example.alittlebetter.presentation.goodthing.OneGoodThingViewModel
import org.example.alittlebetter.presentation.growth.GrowthViewModel
import org.example.alittlebetter.presentation.littlestep.LittleStepViewModel
import org.example.alittlebetter.presentation.memories.MemoriesViewModel
import org.example.alittlebetter.presentation.nightreflection.NightReflectionViewModel
import org.example.alittlebetter.presentation.profile.ProfileViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase(get<SqlDriver>()) }
    single<StepCompletionRepository> { SqlStepCompletionRepository(get()) }
    single<GoodThingRepository> { SqlGoodThingRepository(get()) }
    single<NightReflectionRepository> { SqlNightReflectionRepository(get()) }
    viewModel { LittleStepViewModel(get()) }
    viewModel { OneGoodThingViewModel(get()) }
    viewModel { GrowthViewModel(get()) }
    viewModel { MemoriesViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { NightReflectionViewModel(get()) }
}

fun initKoin(config: KoinAppDeclaration = {}) {
    startKoin {
        config()
        modules(platformModule, appModule)
    }
}