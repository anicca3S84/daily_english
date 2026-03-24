package com.codework.daily_english.presentation.screen.practice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.presentation.screen.practice.model.PracticeCategory
import com.codework.daily_english.presentation.screen.practice.mvi.PracticeEffect
import com.codework.daily_english.presentation.screen.practice.mvi.PracticeEvent
import com.codework.daily_english.presentation.screen.practice.mvi.PracticeState
import com.codework.daily_english.presentation.screen.translate.mvi.TranslateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor() : BaseViewModel<PracticeState, PracticeEvent, PracticeEffect>(
    initialState = PracticeState()
)
{

    init {
        loadPracticeContent()
    }

    override fun onEvent(event: PracticeEvent) {
        when (event) {
            is PracticeEvent.LoadData -> loadPracticeContent()
            is PracticeEvent.OnRetry -> loadPracticeContent()
            is PracticeEvent.OnCategoryClick -> sendEffect(PracticeEffect.NavigateToCategory(event.id))

            // Bắn Effect cho các action mới
            is PracticeEvent.OnFlashcardClick -> sendEffect(PracticeEffect.NavigateToFlashcard)
            is PracticeEvent.OnAiCardClick -> sendEffect(PracticeEffect.NavigateToAiSpeaking(event.type))
            is PracticeEvent.OnGameClick -> sendEffect(PracticeEffect.NavigateToGame(event.gameName))
        }
    }

    private fun loadPracticeContent() {
        viewModelScope.launch {
//            _state.update {
//
//            }
            try {
                delay(1000) // Giả lập load data

                val mockData = PracticeState( // Đổi thành PracticeState
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
//                _state.update {

//                }
            } catch (e: Exception) {
//                _state.update {
//
//                }
            }
        }
    }
}