package org.example.alittlebetter

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.alittlebetter.core.animation.AnimationGallery

@Composable
@Preview
fun App() {
    MaterialTheme {
        AnimationGallery()
    }
}