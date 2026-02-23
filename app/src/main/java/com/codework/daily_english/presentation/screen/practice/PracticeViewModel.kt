package com.codework.daily_english.presentation.screen.practice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.PracticeCategory
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor() : BaseViewModel<PracticeData>() {

    init {
        loadPracticeContent()
    }

    fun onEvent(event: PracticeEvent) {
        when (event) {
            is PracticeEvent.LoadData -> loadPracticeContent()
            is PracticeEvent.OnRetry -> loadPracticeContent()
            is PracticeEvent.OnCategoryClick -> {

            }
        }
    }

    private fun loadPracticeContent() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                delay(1000) // Giả lập load data

                val mockData = PracticeData(
                    streak = 15,
                    totalPoints = 1250,
                    categories = listOf(
                        PracticeCategory(
                            id = "1",
                            title = "Vocabulary",
                            description = "Daily 20 words",
                            icon = Icons.Default.Translate,
                            color = Color(0xFF4CAF50) // Green
                        ),
                        PracticeCategory(
                            id = "2",
                            title = "Listening",
                            description = "Short stories",
                            icon = Icons.Default.Headset,
                            color = Color(0xFF2196F3) // Blue
                        ),
                        PracticeCategory(
                            id = "3",
                            title = "Speaking",
                            description = "Pronunciation",
                            icon = Icons.Default.Mic,
                            color = Color(0xFFFF9800) // Orange
                        ),
                        PracticeCategory(
                            id = "4",
                            title = "Quick Quiz",
                            description = "Test yourself",
                            icon = Icons.Default.Quiz,
                            color = Color(0xFFE91E63) // Pink
                        )
                    )
                )
                _uiState.value = UiState.Success(mockData)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}