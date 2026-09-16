package org.example.alittlebetter.presentation.goodthing

private val gardenFlowers = listOf("🌷", "🌸", "🌼", "🌻", "🌹")

fun flowerForIndex(index: Int): String = gardenFlowers[index % gardenFlowers.size]