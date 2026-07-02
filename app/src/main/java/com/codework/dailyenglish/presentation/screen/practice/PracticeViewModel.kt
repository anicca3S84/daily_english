package com.codework.dailyenglish.presentation.screen.practice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.vocabulary.usecase.GetVocabTopicsUseCase
import com.codework.dailyenglish.presentation.screen.practice.model.PracticeCategory
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEffect
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEvent
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val getVocabTopicsUseCase: GetVocabTopicsUseCase
) : BaseViewModel<PracticeState, PracticeEvent, PracticeEffect>(
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
            is PracticeEvent.OnGameClick -> sendEffect(PracticeEffect.NavigateToGame(event.gameName))
            is PracticeEvent.OnClickLeaderboard -> sendEffect(PracticeEffect.NavigateToLeaderBoard)
            is PracticeEvent.OnExploreMoreAiTopics ->
                sendEffect(PracticeEffect.NavigateToTopicSelection(origin = "AI_PRACTICE"))

            is PracticeEvent.OnAiTopicClick ->
                sendEffect(PracticeEffect.NavigateToAiSpeaking(event.id, event.name))
        }
    }

    private fun loadPracticeContent() {
        _state.update { it.copy(topicsState = it.topicsState.copy(isLoading = true)) }
        viewModelScope.launch {
            getVocabTopicsUseCase()
                .onSuccess { topics ->
                    _state.update { it.copy(topicsState = it.topicsState.copy(data = topics, isLoading = false)) }
                }
                .onError { err ->
                    _state.update { it.copy(topicsState = it.topicsState.copy(isLoading = false, error = err.asUiText())) }
                }
        }
    }
}