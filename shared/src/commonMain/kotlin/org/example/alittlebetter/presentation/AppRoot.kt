package org.example.alittlebetter.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.example.alittlebetter.presentation.goodthing.MyGardenScreen
import org.example.alittlebetter.presentation.goodthing.OneGoodThingScreen
import org.example.alittlebetter.presentation.growth.GrowthScreen
import org.example.alittlebetter.presentation.home.HomeScreen
import org.example.alittlebetter.presentation.letitgo.LetItGoScreen
import org.example.alittlebetter.presentation.littlestep.LittleStepScreen
import org.example.alittlebetter.presentation.memories.MemoriesScreen
import org.example.alittlebetter.presentation.nightreflection.NightReflectionScreen
import org.example.alittlebetter.presentation.profile.ProfileScreen

private sealed interface Destination {
    data object Home : Destination
    data object LittleStep : Destination
    data object OneGoodThing : Destination
    data object Garden : Destination
    data object Growth : Destination
    data object LetItGo : Destination
    data object Memories : Destination
    data object Profile : Destination
    data object NightReflection : Destination
}

/** Hand-rolled screen switcher - a real nav library isn't worth it yet for this few screens. */
@Composable
fun AppRoot() {
    var destination by remember { mutableStateOf<Destination>(Destination.Home) }

    when (destination) {
        Destination.Home -> HomeScreen(
            onLittleStepClick = { destination = Destination.LittleStep },
            onGoodThingClick = { destination = Destination.OneGoodThing },
            onGrowthClick = { destination = Destination.Growth },
            onLetItGoClick = { destination = Destination.LetItGo },
            onMemoriesClick = { destination = Destination.Memories },
            onProfileClick = { destination = Destination.Profile },
        )
        Destination.LittleStep -> LittleStepScreen(onBack = { destination = Destination.Home })
        Destination.OneGoodThing -> OneGoodThingScreen(
            onBack = { destination = Destination.Home },
            onViewGarden = { destination = Destination.Garden },
        )
        Destination.Garden -> MyGardenScreen(onBack = { destination = Destination.Home })
        Destination.Growth -> GrowthScreen(onBack = { destination = Destination.Home })
        Destination.LetItGo -> LetItGoScreen(onBack = { destination = Destination.Home })
        Destination.Memories -> MemoriesScreen(onBack = { destination = Destination.Home })
        Destination.Profile -> ProfileScreen(
            onBack = { destination = Destination.Home },
            onNightReflectionClick = { destination = Destination.NightReflection },
        )
        Destination.NightReflection -> NightReflectionScreen(onBack = { destination = Destination.Home })
    }
}