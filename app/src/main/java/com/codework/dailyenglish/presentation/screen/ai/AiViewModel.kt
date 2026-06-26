package com.codework.dailyenglish.presentation.screen.ai

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.model.ChatSession
import com.codework.dailyenglish.domain.ai.usecase.GetHistorySessionsUseCase
import com.codework.dailyenglish.domain.ai.usecase.SendMessageUseCase
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEffect
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEvent
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiState
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getHistorySessionsUseCase: GetHistorySessionsUseCase
) : BaseViewModel<AiState, AiEvent, AiEffect>(AiState()) {

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            when (val result = getHistorySessionsUseCase()) {
                is AppResult.Success -> {
                    _state.update { it.copy(historySessions = result.data) }
                }
                is AppResult.Error -> {
                    sendEffect(AiEffect.ShowToast("Failed to load history"))
                }
            }
        }
    }

    override fun onEvent(event: AiEvent) {
        when (event) {
            is AiEvent.OnSendMessage -> sendMessage(event.message)
            is AiEvent.OnSuggestionClick -> sendMessage(event.text)
            is AiEvent.OnNewChat -> {
                _state.update { it.copy(messages = emptyList(), currentSessionId = null) }
            }
            is AiEvent.OnLoadSession -> { /* Gọi UseCase load tin nhắn của session đó */ }
            is AiEvent.OnImageClick -> sendEffect(AiEffect.OpenGallery)
            is AiEvent.OnMicClick -> sendEffect(AiEffect.OpenMicrophone)
        }
    }

    private fun sendMessage(text: String) {
        if (text.isBlank()) return

        val sessionId = _state.value.currentSessionId ?: "new"

        val userMsg = ChatMessage(text = text, isUser = true)
        _state.update { it.copy(messages = it.messages + userMsg, isTyping = true) }

        viewModelScope.launch {
            when (val result = sendMessageUseCase(sessionId, text)) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            messages = it.messages + result.data,
                            isTyping = false
                        )
                    }
                }
                is AppResult.Error -> {
                    _state.update { it.copy(isTyping = false) }
                    sendEffect(AiEffect.ShowToast("Cannot connect to AI server."))
                }
            }
        }
    }
}