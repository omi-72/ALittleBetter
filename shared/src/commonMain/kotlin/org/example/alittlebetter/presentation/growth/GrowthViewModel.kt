package org.example.alittlebetter.presentation.growth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.alittlebetter.domain.StepCompletionRepository

data class GrowthUiState(
    val isLoading: Boolean = true,
    val daysCompleted: Int = 0,
)

class GrowthViewModel(private val repository: StepCompletionRepository) : ViewModel() {
    private val _state = MutableStateFlow(GrowthUiState())
    val state: StateFlow<GrowthUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val days = repository.countCompletedDays()
            _state.update { it.copy(isLoading = false, daysCompleted = days) }
        }
    }
}
