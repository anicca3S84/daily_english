package com.codework.dailyenglish.presentation.screen.ai

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.utils.VoiceToTextParser
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.model.ChatSession
import com.codework.dailyenglish.domain.ai.usecase.GetHistorySessionsUseCase
import com.codework.dailyenglish.domain.ai.usecase.SendMessageUseCase
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEffect
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEvent
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiState
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getHistorySessionsUseCase: GetHistorySessionsUseCase,
    private val voiceToTextParser: VoiceToTextParser
) : BaseViewModel<AiState, AiEvent, AiEffect>(AiState()) {

    init {
        loadHistory()
        observeVoiceInput() // Lắng nghe giọng nói
    }

    private fun loadHistory() {
        viewModelScope.launch {
            when (val result = getHistorySessionsUseCase()) {
                is AppResult.Success -> _state.update { it.copy(historySessions = result.data) }
                is AppResult.Error -> sendEffect(AiEffect.ShowToast("Failed to load history"))
            }
        }
    }



    private fun sendMessage(text: String) {
        if (text.isBlank()) return

        val sessionId = _state.value.currentSessionId ?: "new"
        val userMsg = ChatMessage(text = text, isUser = true)

        // Khi gửi tin nhắn, reset text thu âm và bật typing indicator
        _state.update { it.copy(messages = it.messages + userMsg, isTyping = true, voiceCapturedText = "") }

        viewModelScope.launch {
            when (val result = sendMessageUseCase(sessionId, text, isNeedSuggestions = false)) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            messages = it.messages + result.data,
                            currentSessionId = result.data.id.ifBlank { it.currentSessionId },
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

    override fun onEvent(event: AiEvent) {
        when (event) {
            is AiEvent.OnSendMessage -> sendMessage(event.message)
            is AiEvent.OnSuggestionClick -> sendMessage(event.text)
            AiEvent.OnNewChat -> _state.update { it.copy(messages = emptyList(), currentSessionId = null) }
            is AiEvent.OnLoadSession -> { /* Load session messages */ }
            AiEvent.OnImageClick -> sendEffect(AiEffect.OpenGallery)
            AiEvent.OnMicToggle -> toggleVoiceRecording()
            else -> {}
        }
    }

    private fun observeVoiceInput() {
        viewModelScope.launch {
            voiceToTextParser.state.collectLatest { voiceState ->
                // 1. Đồng bộ trạng thái ghi âm lên UI
                if (_state.value.isVoiceRecording != voiceState.isListening) {
                    _state.update { it.copy(isVoiceRecording = voiceState.isListening) }
                }

                // 2. Cập nhật real-time vào khung nhập liệu
                if (voiceState.spokenText.isNotBlank()) {
                    _state.update { it.copy(voiceCapturedText = voiceState.spokenText) }
                }

                // 3. ĐIỀU KIỆN GỬI TIN NHẮN AN TOÀN:
                // Chỉ gửi tin nhắn đi khi:
                // - Trạng thái lắng nghe đã kết thúc (isListening == false)
                // - KHÔNG có lỗi xảy ra (voiceState.error == null) - Tránh việc lỗi timeout tự gửi tin nhắn cũ
                // - Văn bản thu được không trống và khớp với text đang hiển thị trên UI
                if (!voiceState.isListening &&
                    voiceState.error == null &&
                    voiceState.spokenText.isNotBlank() &&
                    _state.value.voiceCapturedText == voiceState.spokenText
                ) {
                    val finalSpeechText = voiceState.spokenText

                    // Reset text tạm thời trên UI State
                    _state.update { it.copy(voiceCapturedText = "") }

                    // Thực hiện gửi tin nhắn đi
                    sendMessage(finalSpeechText)

                    // Dọn dẹp mic an toàn
                    voiceToTextParser.stopListening()
                }

                // Hiển thị thông báo nếu mic gặp lỗi ngoại lệ (bỏ qua thông báo timeout vô hại)
                voiceState.error?.let { err ->
                    if (err != "No speech input" && err != "No match found") {
                        sendEffect(AiEffect.ShowToast(err))
                    }
                }
            }
        }
    }

    private fun toggleVoiceRecording() {
        val nextRecordingState = !_state.value.isVoiceRecording
        if (nextRecordingState) {
            voiceToTextParser.startListening(languageCode = "en-US")
        } else {
            voiceToTextParser.stopListening()
        }
    }


    override fun onCleared() {
        super.onCleared()
        voiceToTextParser.release()
    }
}