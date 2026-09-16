package org.example.alittlebetter.presentation.memories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import org.example.alittlebetter.core.time.currentLocalDate
import org.example.alittlebetter.domain.GoodThingRepository
import org.example.alittlebetter.domain.StepCompletionRepository

data class CalendarCell(
    val date: LocalDate?,
    val hasLittleStep: Boolean = false,
    val hasGoodThing: Boolean = false,
)

data class MemoriesUiState(
    val isLoading: Boolean = true,
    val monthLabel: String = "",
    val cells: List<CalendarCell> = emptyList(),
)

class MemoriesViewModel(
    private val stepCompletionRepository: StepCompletionRepository,
    private val goodThingRepository: GoodThingRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MemoriesUiState())
    val state: StateFlow<MemoriesUiState> = _state.asStateFlow()

    private var completedDates: Set<LocalDate> = emptySet()
    private var goodThingDates: Set<LocalDate> = emptySet()
    private var visibleMonth: LocalDate = firstOfMonth(currentLocalDate())

    init {
        viewModelScope.launch {
            completedDates = stepCompletionRepository.getCompletedDates().toSet()
            goodThingDates = goodThingRepository.getAllGoodThings().map { it.date }.toSet()
            renderMonth()
        }
    }

    fun showPreviousMonth() {
        visibleMonth = visibleMonth.plus(DatePeriod(months = -1))
        renderMonth()
    }

    fun showNextMonth() {
        visibleMonth = visibleMonth.plus(DatePeriod(months = 1))
        renderMonth()
    }

    private fun renderMonth() {
        val nextMonth = visibleMonth.plus(DatePeriod(months = 1))
        val daysInMonth = visibleMonth.daysUntil(nextMonth)
        val leadingBlanks = visibleMonth.dayOfWeek.ordinal

        val cells = buildList {
            repeat(leadingBlanks) { add(CalendarCell(date = null)) }
            repeat(daysInMonth) { offset ->
                val date = visibleMonth.plus(DatePeriod(days = offset))
                add(
                    CalendarCell(
                        date = date,
                        hasLittleStep = date in completedDates,
                        hasGoodThing = date in goodThingDates,
                    ),
                )
            }
        }

        _state.update {
            it.copy(isLoading = false, monthLabel = monthLabelFor(visibleMonth), cells = cells)
        }
    }
}

private fun firstOfMonth(date: LocalDate): LocalDate = LocalDate(date.year, date.month, 1)

private fun monthLabelFor(month: LocalDate): String {
    val name = month.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$name ${month.year}"
}
