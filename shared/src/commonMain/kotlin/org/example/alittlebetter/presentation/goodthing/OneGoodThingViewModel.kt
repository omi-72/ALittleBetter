package org.example.alittlebetter.presentation.goodthing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.alittlebetter.domain.GoodThingRepository

enum class OneGoodThingPhase { WRITING, SAVED }

data class OneGoodThingUiState(
    val text: String = "",
    val phase: OneGoodThingPhase = OneGoodThingPhase.WRITING,
    val savedFlowerIndex: Int = 0,
)

class OneGoodThingViewModel(private val repository: GoodThingRepository) : ViewModel() {
    private val _state = MutableStateFlow(OneGoodThingUiState())
    val state: StateFlow<OneGoodThingUiState> = _state.asStateFlow()

    fun updateText(text: String) {
        _state.update { it.copy(text = text) }
    }

    fun save() {
        val current = _state.value
        if (current.text.isBlank() || current.phase == OneGoodThingPhase.SAVED) return
        viewModelScope.launch {
            repository.addGoodThing(current.text.trim())
            val savedCount = repository.getAllGoodThings().size
            _state.update { it.copy(phase = OneGoodThingPhase.SAVED, savedFlowerIndex = savedCount - 1) }
        }
    }
}