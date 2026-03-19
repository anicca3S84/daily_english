package com.codework.daily_english.presentation.screen.ai

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.core.base.BaseState
import com.codework.daily_english.presentation.screen.ai.model.ChatMessage
import com.codework.daily_english.presentation.screen.ai.model.ChatSession
import com.codework.daily_english.presentation.screen.ai.mvi.AiEffect
import com.codework.daily_english.presentation.screen.ai.mvi.AiEvent
import com.codework.daily_english.presentation.screen.ai.mvi.AiState
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val generativeModel: GenerativeModel
) : BaseViewModel<AiState, AiEvent, AiEffect>() {

    private var chat = generativeModel.startChat()

    private val mockHistory = listOf(
        ChatSession("1", "Grammar Check", "Is this sentence correct?", "Today"),
        ChatSession("2", "Grammar Check", "Is this sentence correct?", "Yesterday"),
        ChatSession("3", "Grammar Check", "Is this sentence correct?", "Last week"),
    )

    init {
        _uiState.update {
            BaseState.Success(
                AiState(
                    messages = emptyList(),
                    historySessions = mockHistory,
                    currentSessionId = UUID.randomUUID().toString()
                )
            )
        }
    }

    override fun onEvent(event: AiEvent) {
        val currentState = (_uiState.value as? BaseState.Success)?.data ?: return

        when (event) {
            is AiEvent.OnSendMessage -> sendMessage(event.message)
            is AiEvent.OnSuggestionClick -> sendMessage(event.text)

            is AiEvent.OnNewChat -> {
                chat = generativeModel.startChat()
                _uiState.update {
                    BaseState.Success(
                        currentState.copy(
                            messages = emptyList(),
                            currentSessionId = UUID.randomUUID().toString()
                        )
                    )
                }
            }

            is AiEvent.OnLoadSession -> { /* Xử lý load session cũ từ DB */ }

            is AiEvent.OnImageClick -> sendEffect(AiEffect.OpenGallery)
            is AiEvent.OnMicClick -> sendEffect(AiEffect.OpenMicrophone)
        }
    }

    private fun sendMessage(text: String) {
        if (text.isBlank()) return
        val currentState = (_uiState.value as? BaseState.Success)?.data ?: return

        val newMessages = currentState.messages + ChatMessage(text = text, isUser = true)
        _uiState.update { BaseState.Success(currentState.copy(messages = newMessages, isTyping = true)) }

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    chat.sendMessage(text)
                }
                val aiReplyText = response.text ?: "Sorry, I didn't understand that."
                val updatedMessages = newMessages + ChatMessage(text = aiReplyText, isUser = false)

                _uiState.update { BaseState.Success(currentState.copy(messages = updatedMessages, isTyping = false)) }
            } catch (e: Exception) {
                e.printStackTrace()
                // Báo lỗi cho người dùng bằng Effect Toast thay vì in lỗi chình ình vào khung chat
                sendEffect(AiEffect.ShowToast("Cannot connect to AI. Please check your internet."))

                // Khôi phục lại UI tắt chữ Typing
                _uiState.update { BaseState.Success(currentState.copy(isTyping = false)) }
            }
        }
    }
}