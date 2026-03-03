package com.codework.daily_english.presentation.screen.grammar

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.GrammarTip
import com.codework.daily_english.presentation.model.GrammarTopic
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrammarViewModel @Inject constructor() : BaseViewModel<GrammarData>() {

    // Danh sách gốc (Mock DB)
    private var allTopics = listOf<GrammarTopic>()

    init {
        loadGrammarData()
    }

    fun onEvent(event: GrammarEvent) {
        when (event) {
            is GrammarEvent.OnTopicClick -> {
                navigateTo(Route.LessonScreen)
            }
            is GrammarEvent.OnLevelFilterSelected -> {
                filterTopics(event.level)
            }
            is GrammarEvent.OnBackClick -> {
                viewModelScope.launch { _navigationChannel.send(Route.HomeScreen) }
            }
            is GrammarEvent.OnSearchClick -> {
                // Handle search later
            }
        }
    }

    private fun loadGrammarData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(500) // Giả lập network delay

            // Mock Data
            allTopics = listOf(
                GrammarTopic("1", "Present Simple", "Talk about habits and facts.", "A1", 1.0f, false),
                GrammarTopic("2", "Present Continuous", "Actions happening now.", "A1", 0.8f, false),
                GrammarTopic("3", "Past Simple", "Completed actions in the past.", "A2", 0.4f, false),
                GrammarTopic("4", "Past Continuous", "Actions in progress in the past.", "A2", 0.0f, false),
                GrammarTopic("5", "Present Perfect", "Experience and recent actions.", "B1", 0.0f, true),
                GrammarTopic("6", "Future Forms", "Will, Going to, and Present Continuous.", "B1", 0.0f, true),
                GrammarTopic("7", "Conditionals", "Zero, First, Second conditionals.", "B2", 0.0f, true),
                GrammarTopic("8", "Passive Voice", "Focus on the action, not the doer.", "B2", 0.0f, true)
            )

            val tip = GrammarTip(
                title = "Did you know?",
                content = "We use 'at' for precise times (at 3pm), 'on' for days (on Monday), and 'in' for months/years (in 2024)."
            )

            _uiState.value = UiState.Success(
                GrammarData(
                    selectedLevel = "All",
                    grammarTopics = allTopics,
                    dailyTip = tip
                )
            )
        }
    }

    private fun filterTopics(level: String) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        val filteredList = if (level == "All") {
            allTopics
        } else {
            allTopics.filter { it.level == level }
        }

        _uiState.value = UiState.Success(
            currentState.copy(
                selectedLevel = level,
                grammarTopics = filteredList
            )
        )
    }
}