package org.example.alittlebetter

import androidx.compose.ui.window.ComposeUIViewController
import org.example.alittlebetter.core.di.initKoin

private var koinStarted = false

fun MainViewController() = ComposeUIViewController {
    if (!koinStarted) {
        initKoin()
        koinStarted = true
    }
    App()
}