package org.example.alittlebetter

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.alittlebetter.core.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ALittleBetter",
        ) {
            App()
        }
    }
}