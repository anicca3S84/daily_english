package com.codework.dailyenglish.presentation.screen.ai_speaking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.usecase.SendMessageUseCase
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEffect
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEvent
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiSpeakingViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AiSpeakingState, AiSpeakingEvent, AiSpeakingEffect>(AiSpeakingState()) {

    private val topicId = checkNotNull(savedStateHandle.get<String>("topicId"))
    private val topicName = checkNotNull(savedStateHandle.get<String>("topicName"))

    init {
        _state.update { it.copy(topicName = topicName) }
        // Gửi System Prompt để định hình AI là partner luyện nói
        sendMessage("System: You are an English speaking partner. Let's talk about $topicName.", isSystem = true)
    }

    override fun onEvent(event: AiSpeakingEvent) {
        when (event) {
            is AiSpeakingEvent.OnSendMessage -> sendMessage(event.text)
            AiSpeakingEvent.OnToggleRecording -> {
                val newRecordingState = !_state.value.isRecording
                _state.update { it.copy(isRecording = newRecordingState) }
                if (newRecordingState) sendEffect(AiSpeakingEffect.StartSpeechRecognition)
            }
            AiSpeakingEvent.OnToggleSuggestions -> {
                // Logic mở/đóng menu gợi ý
            }
        }
    }

    private fun sendMessage(text: String, isSystem: Boolean = false) {
        val userMsg = if (!isSystem) ChatMessage(text = text, isUser = true) else null
        if (userMsg != null) {
            _state.update { it.copy(messages = it.messages + userMsg, isTyping = true) }
        }

        viewModelScope.launch {
            sendMessageUseCase("session_id_here", text) // Thay sessionId thật của bạn
                .onSuccess { aiMsg ->
                    _state.update { it.copy(messages = it.messages + aiMsg, isTyping = false) }
                }
                .onError { err ->
                    _state.update { it.copy(isTyping = false) }
                    sendGlobalEffect(BaseEffect.ShowToast(err.asUiText()))
                }
        }
    }
}