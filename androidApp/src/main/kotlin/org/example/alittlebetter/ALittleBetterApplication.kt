package org.example.alittlebetter

import android.app.Application
import org.example.alittlebetter.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class ALittleBetterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ALittleBetterApplication)
        }
    }
}