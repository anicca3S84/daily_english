package com.codework.dailyenglish.presentation.screen.ai_speaking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.usecase.SendMessageUseCase
import com.codework.dailyenglish.core.utils.TextToSpeechParser
import com.codework.dailyenglish.core.utils.VoiceToTextParser
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEffect
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEvent
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingState
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingUiData
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AiSpeakingViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val textToSpeechParser: TextToSpeechParser,
    private val voiceToTextParser: VoiceToTextParser,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AiSpeakingState, AiSpeakingEvent, AiSpeakingEffect>(
    AiSpeakingState(uiState = BaseUiState(data = AiSpeakingUiData(), isLoading = true))
) {

    private val topicId = checkNotNull(savedStateHandle.get<String>("topicId"))
    private val topicName = checkNotNull(savedStateHandle.get<String>("topicName"))

    init {
        // 1. Nạp trước dữ liệu topicName vào data của State
        _state.update { state ->
            state.copy(
                uiState = state.uiState.copy(
                    data = state.uiState.data?.copy(topicName = topicName) ?: AiSpeakingUiData(topicName = topicName)
                )
            )
        }

        // 2. Kích hoạt quan sát luồng nhận diện giọng nói từ VoiceToTextParser
        observeVoiceInput()

        // 3. Gửi tin nhắn chào mừng ban đầu
        sendInitMessage()
    }

    override fun onEvent(event: AiSpeakingEvent) {
        when (event) {
            is AiSpeakingEvent.OnSendMessage -> {
                _state.update { it.copy(isSuggestionsVisible = false) }
                sendMessage(event.text)
            }
            is AiSpeakingEvent.OnToggleRecording -> {
                toggleRecording()
            }
            is AiSpeakingEvent.OnToggleSuggestions -> {
                _state.update { it.copy(isSuggestionsVisible = !_state.value.isSuggestionsVisible) }
            }
            is AiSpeakingEvent.OnRetryChat -> {
                _state.update { state ->
                    state.copy(uiState = state.uiState.copy(isLoading = true, error = null))
                }
                sendInitMessage()
            }
        }
    }

    /**
     * Đồng bộ trạng thái ghi âm và điều khiển VoiceToTextParser
     */
    private fun toggleRecording() {
        val nextRecordingState = !_state.value.isRecording
        _state.update { it.copy(isRecording = nextRecordingState) }

        if (nextRecordingState) {
            // Dừng phát âm của AI ngay lập tức nếu người dùng muốn nói chèn vào
            textToSpeechParser.release()
            voiceToTextParser.startListening(languageCode = "en-US")
            sendEffect(AiSpeakingEffect.StartSpeechRecognition)
        } else {
            voiceToTextParser.stopListening()
        }
    }

    /**
     * Lắng nghe kết quả Text trả về từ Mic của SpeechRecognizer
     */
    private fun observeVoiceInput() {
        viewModelScope.launch {
            voiceToTextParser.state.collectLatest { voiceState ->
                // Đồng bộ cờ hiển thị hiệu ứng Mic nhấp nháy theo hệ thống ngắt tự động
                if (_state.value.isRecording != voiceState.isListening) {
                    _state.update { it.copy(isRecording = voiceState.isListening) }
                }

                // Xử lý khi có lỗi xảy ra từ Mic (ví dụ: Không nói gì / Quá thời gian im lặng)
                voiceState.error?.let { errorMsg ->
                    sendGlobalEffect(BaseEffect.ShowToast(com.codework.dailyenglish.presentation.utils.UiText.DynamicString(errorMsg)))
                }

                // Khi Mic trả về kết quả cuối cùng (đã ngắt và isListening chuyển sang false)
                if (!voiceState.isListening && voiceState.spokenText.isNotBlank()) {
                    val capturedText = voiceState.spokenText
                    // Reset text đã nhận diện trong Parser tránh bị lặp lại ở luồng Collect tiếp theo
                    voiceToTextParser.release()

                    // Thực hiện gửi text đi sang luồng AI xử lý
                    sendMessage(capturedText)
                }
            }
        }
    }

    private fun sendInitMessage() {
        val welcomeText = "Hello! Let's practice speaking English about the topic: $topicName."
        sendMessageToServer(welcomeText, isSystemInit = true)
    }

    private fun sendMessage(text: String) {
        val currentData = _state.value.uiState.data ?: AiSpeakingUiData()
        val userMsg = ChatMessage(text = text, isUser = true)

        _state.update { state ->
            state.copy(
                uiState = state.uiState.copy(
                    isLoading = true,
                    data = currentData.copy(messages = currentData.messages + userMsg)
                )
            )
        }
        sendMessageToServer(text, isSystemInit = false)
    }

    private fun sendMessageToServer(text: String, isSystemInit: Boolean) {
        val currentData = _state.value.uiState.data ?: AiSpeakingUiData()

        viewModelScope.launch {
            sendMessageUseCase(
                sessionId = currentData.currentSessionId,
                message = text,
                topicId = topicId,
                topicName = topicName,
                isNeedSuggestions = true
            ).onSuccess { aiMsg ->
                _state.update { state ->
                    val updatedData = (state.uiState.data ?: AiSpeakingUiData()).copy(
                        currentSessionId = aiMsg.id.ifBlank { currentData.currentSessionId },
                        messages = (state.uiState.data?.messages.orEmpty()) + aiMsg,
                        suggestions = aiMsg.suggestions
                    )
                    state.copy(
                        uiState = BaseUiState(data = updatedData, isLoading = false, error = null)
                    )
                }

                // [TTS INTEGRATION]: AI trả lời thành công -> Tự động gọi đọc chuỗi Text lên thành tiếng
                playAiResponse(aiMsg.text)

            }.onError { err ->
            _state.update { state ->
                if (isSystemInit && currentData.messages.isEmpty()) {
                    state.copy(uiState = BaseUiState<AiSpeakingUiData>(isLoading = false, error = err.asUiText()))
                } else {
                    state.copy(uiState = state.uiState.copy(isLoading = false))
                }
            }
            if (!isSystemInit || currentData.messages.isNotEmpty()) {
                sendGlobalEffect(BaseEffect.ShowToast(err.asUiText()))
            }
        }
        }
    }

    /**
     * Sử dụng TextToSpeechParser để phát âm tin nhắn của AI
     */
    private fun playAiResponse(content: String) {
        textToSpeechParser.playAudio(
            url = null, // Truyền null để ép hàm tự động chuyển hướng dùng engine TTS
            textToSpeak = content,
            speed = 1.0f,
            locale = Locale.US,
            scope = viewModelScope
        )
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeechParser.release()
        voiceToTextParser.release()
    }
}