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
import org.example.alittlebetter.presentation.littlestep.LittleStepScreen

private sealed interface Destination {
    data object Home : Destination
    data object LittleStep : Destination
    data object OneGoodThing : Destination
    data object Garden : Destination
    data object Growth : Destination
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
        )
        Destination.LittleStep -> LittleStepScreen(onBack = { destination = Destination.Home })
        Destination.OneGoodThing -> OneGoodThingScreen(
            onBack = { destination = Destination.Home },
            onViewGarden = { destination = Destination.Garden },
        )
        Destination.Garden -> MyGardenScreen(onBack = { destination = Destination.Home })
        Destination.Growth -> GrowthScreen(onBack = { destination = Destination.Home })
    }
}