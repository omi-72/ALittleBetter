package org.example.alittlebetter.presentation.littlestep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.alittlebetter.core.time.currentLocalDate
import org.example.alittlebetter.domain.StepCompletionRepository

private const val TOTAL_SECONDS = 600

enum class LittleStepPhase { IDLE, RUNNING, FINISHED }

data class LittleStepUiState(
    val phase: LittleStepPhase = LittleStepPhase.IDLE,
    val secondsRemaining: Int = TOTAL_SECONDS,
)

class LittleStepViewModel(private val repository: StepCompletionRepository) : ViewModel() {
    private val _state = MutableStateFlow(LittleStepUiState())
    val state: StateFlow<LittleStepUiState> = _state.asStateFlow()

    private var timerJob: Job? = null

    fun start() {
        if (_state.value.phase != LittleStepPhase.IDLE) return
        _state.update { it.copy(phase = LittleStepPhase.RUNNING, secondsRemaining = TOTAL_SECONDS) }
        timerJob = viewModelScope.launch {
            while (_state.value.secondsRemaining > 0) {
                delay(1_000)
                _state.update { it.copy(secondsRemaining = it.secondsRemaining - 1) }
            }
            _state.update { it.copy(phase = LittleStepPhase.FINISHED) }
            repository.recordCompletion(currentLocalDate())
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
    }
}