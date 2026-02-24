package com.codework.daily_english.presentation.screen.lessons.listening

import com.codework.daily_english.presentation.model.DialogueLine


import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.QuizType
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListeningViewModel @Inject constructor() : BaseViewModel<ListeningData>() {

    init {
        loadDialogue()
    }

    fun onEvent(event: ListeningEvent) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        when (event) {
            is ListeningEvent.TogglePlayPause -> {
                _uiState.value = UiState.Success(currentState.copy(isPlaying = !currentState.isPlaying))
                // Gắn logic Play/Pause Audio thật ở đây
            }
            is ListeningEvent.ToggleSpeed -> {
                _uiState.value = UiState.Success(currentState.copy(isSlowMode = !currentState.isSlowMode))
                // Gắn logic đổi tốc độ Audio thật ở đây (ex: 0.75x)
            }
            is ListeningEvent.OnSeek -> {
                val newTime = (event.position * currentState.duration).toLong()
                _uiState.value = UiState.Success(currentState.copy(currentTime = newTime))
                // Gắn logic seek Audio thật ở đây
            }
            is ListeningEvent.OnLetsPracticeClick -> {
                navigateTo(Route.QuizReviewScreen(QuizType.LISTENING))
            }
        }
    }

    private fun loadDialogue() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(500) // Giả lập call API

            val mockData = ListeningData(
                title = "Ordering your favorite coffee",
                subtitle = "Listen and follow the transcript",
                imageUrl = "https://picsum.photos/400/200", // Đổi link ảnh của bạn vào đây
                duration = 45000L, // 45 giây
                currentTime = 12000L, // Giả sử đang phát đến giây thứ 12 để test UI highlight
                isPlaying = false,
                isSlowMode = false,
                progressCurrent = 1,
                progressTotal = 5,
                dialogues = listOf(
                    DialogueLine("Barista", "👨‍🍳", "Hi, what can I get for you today?", 0L, 4000L),
                    DialogueLine("Customer", "👩", "I'd like an iced latte, please.", 4500L, 8000L),
                    DialogueLine("Barista", "👨‍🍳", "What size would you like?", 8500L, 11000L),
                    DialogueLine("Customer", "👩", "A medium, please. With oat milk if possible.", 11500L, 16000L),
                    DialogueLine("Barista", "👨‍🍳", "Sure thing. That'll be $4.50.", 16500L, 20000L)
                )
            )
            _uiState.value = UiState.Success(mockData)
        }
    }
}