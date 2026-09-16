package org.example.alittlebetter.presentation.nightreflection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.alittlebetter.core.time.currentLocalDate
import org.example.alittlebetter.domain.NightReflectionRepository
import org.example.alittlebetter.domain.nightReflectionQuestions

enum class NightReflectionPhase { ANSWERING, DONE }

data class NightReflectionUiState(
    val phase: NightReflectionPhase = NightReflectionPhase.ANSWERING,
    val questionIndex: Int = 0,
    val currentAnswer: String = "",
) {
    val totalQuestions: Int get() = nightReflectionQuestions.size
    val currentQuestion: String get() = nightReflectionQuestions[questionIndex]
    val isLastQuestion: Boolean get() = questionIndex == totalQuestions - 1
}

class NightReflectionViewModel(private val repository: NightReflectionRepository) : ViewModel() {
    private val _state = MutableStateFlow(NightReflectionUiState())
    val state: StateFlow<NightReflectionUiState> = _state.asStateFlow()

    fun updateAnswer(text: String) {
        _state.update { it.copy(currentAnswer = text) }
    }

    fun next() {
        val current = _state.value
        viewModelScope.launch {
            if (current.currentAnswer.isNotBlank()) {
                repository.saveAnswer(currentLocalDate(), current.currentQuestion, current.currentAnswer.trim())
            }
            _state.update {
                if (current.isLastQuestion) {
                    it.copy(phase = NightReflectionPhase.DONE)
                } else {
                    it.copy(questionIndex = it.questionIndex + 1, currentAnswer = "")
                }
            }
        }
    }
}
