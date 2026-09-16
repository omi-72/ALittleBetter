package org.example.alittlebetter.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.alittlebetter.core.animation.GrowthStage
import org.example.alittlebetter.core.animation.growthStageFor
import org.example.alittlebetter.domain.GoodThingRepository
import org.example.alittlebetter.domain.StepCompletionRepository

data class ProfileUiState(
    val isLoading: Boolean = true,
    val littleStepsCount: Int = 0,
    val goodThingsCount: Int = 0,
    val growthStage: GrowthStage = GrowthStage.SEED,
)

class ProfileViewModel(
    private val stepCompletionRepository: StepCompletionRepository,
    private val goodThingRepository: GoodThingRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val littleSteps = stepCompletionRepository.countCompletedDays()
            val goodThings = goodThingRepository.getAllGoodThings().size
            _state.update {
                it.copy(
                    isLoading = false,
                    littleStepsCount = littleSteps,
                    goodThingsCount = goodThings,
                    growthStage = growthStageFor(littleSteps),
                )
            }
        }
    }
}
