package com.codework.dailyenglish.presentation.screen.ai_speaking

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.core.network.AppError
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onErrorDetails
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.usecase.SendAgentMessageUseCase // SỬ DỤNG USE CASE MỚI CỦA AGENT
import com.codework.dailyenglish.domain.auth.usecase.GetMyProfileUseCase
import com.codework.dailyenglish.domain.user.usecase.GetUserInfoUseCase
import com.codework.dailyenglish.core.utils.TextToSpeechParser
import com.codework.dailyenglish.core.utils.VoiceToTextParser
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEffect
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEvent
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingState
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingUiData
import com.codework.dailyenglish.presentation.utils.UiText
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AiSpeakingViewModel @Inject constructor(
    private val sendAgentMessageUseCase: SendAgentMessageUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val textToSpeechParser: TextToSpeechParser,
    private val voiceToTextParser: VoiceToTextParser,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AiSpeakingState, AiSpeakingEvent, AiSpeakingEffect>(
    AiSpeakingState(uiState = BaseUiState(data = AiSpeakingUiData(), isLoading = true))
) {

    private val topicId = checkNotNull(savedStateHandle.get<String>("topicId"))
    private val topicName = checkNotNull(savedStateHandle.get<String>("topicName"))

    init {
        _state.update { state ->
            state.copy(
                uiState = state.uiState.copy(
                    data = state.uiState.data?.copy(topicName = topicName) ?: AiSpeakingUiData(topicName = topicName)
                )
            )
        }
        checkUserPremiumStatus()
        observeVoiceInput()
        sendInitMessage()
    }

    private fun checkUserPremiumStatus() {
        viewModelScope.launch {
            val localUser = getUserInfoUseCase()
            val initialPremium = localUser?.isPremium ?: false
            _state.update {
                it.copy(
                    isPremium = initialPremium,
                    // Giới hạn mặc định của AI Agent: Free = 3, Premium = 50
                    remainingMessages = if (initialPremium) 50 else it.remainingMessages,
                    maxCharLimit = if (initialPremium) 300 else 150
                )
            }

            getMyProfileUseCase().onSuccess { freshUser ->
                _state.update {
                    it.copy(
                        isPremium = freshUser.isPremium,
                        remainingMessages = if (freshUser.isPremium) 50 else it.remainingMessages,
                        maxCharLimit = if (freshUser.isPremium) 300 else it.maxCharLimit
                    )
                }
            }
        }
    }

    override fun onEvent(event: AiSpeakingEvent) {
        when (event) {
            is AiSpeakingEvent.OnSendMessage -> {
                _state.update { it.copy(isSuggestionsVisible = false) }
                sendMessage(event.text)
            }
            is AiSpeakingEvent.OnToggleRecording -> toggleRecording(event.context)
            is AiSpeakingEvent.OnToggleSuggestions -> {
                _state.update { it.copy(isSuggestionsVisible = !_state.value.isSuggestionsVisible) }
            }
            is AiSpeakingEvent.OnRetryChat -> {
                _state.update { state ->
                    state.copy(uiState = state.uiState.copy(isLoading = true, error = null))
                }
                sendInitMessage()
            }
            is AiSpeakingEvent.OnStopRecording -> stopRecording()
            is AiSpeakingEvent.OnDismissLimitConfirmDialog -> {
                _state.update { it.copy(isShowLimitConfirmDialog = false) }
            }
            is AiSpeakingEvent.OnConfirmLimitUpgrade -> {
                _state.update {
                    it.copy(
                        isShowLimitConfirmDialog = false,
                        isShowPremiumUpgradeDialog = true
                    )
                }
            }
            is AiSpeakingEvent.OnDismissSubscriptionPopup -> {
                _state.update { it.copy(isShowPremiumUpgradeDialog = false) }
                checkUserPremiumStatus()
            }
        }
    }

    private fun stopRecording() {
        _state.update { it.copy(isRecording = false) }
        voiceToTextParser.stopListening()
    }

    private fun toggleRecording(context: Context) {
        val nextRecordingState = !_state.value.isRecording
        _state.update { it.copy(isRecording = nextRecordingState) }

        if (nextRecordingState) {
            textToSpeechParser.release()
            voiceToTextParser.startListening(context = context, languageCode = "en-US")
        } else {
            voiceToTextParser.stopListening()
        }
    }

    private fun observeVoiceInput() {
        viewModelScope.launch {
            voiceToTextParser.state.collectLatest { voiceState ->
                if (_state.value.isRecording != voiceState.isListening) {
                    _state.update { it.copy(isRecording = voiceState.isListening) }
                }

                voiceState.error?.let { errorMsg ->
                    sendGlobalEffect(
                        BaseEffect.ShowToast(
                            errorMsg
                        )
                    )
                }

                if (!voiceState.isListening && voiceState.spokenText.isNotBlank()) {
                    val capturedText = voiceState.spokenText
                    voiceToTextParser.release()
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
        val trimmedText = text.trim()
        if (trimmedText.isBlank()) return

        val currentState = _state.value

        if (trimmedText.length > currentState.maxCharLimit) {
            sendGlobalEffect(BaseEffect.ShowToast(UiText.DynamicString("Your message exceeds the limit of ${currentState.maxCharLimit} characters.")))
            return
        }

        if (!currentState.isPremium && currentState.remainingMessages <= 0) {
            _state.update { it.copy(isShowLimitConfirmDialog = true) }
            return
        }

        val currentData = currentState.uiState.data ?: AiSpeakingUiData()
        val userMsg = ChatMessage(text = trimmedText, isUser = true)

        _state.update { state ->
            state.copy(
                uiState = state.uiState.copy(
                    isLoading = true,
                    data = currentData.copy(messages = currentData.messages + userMsg)
                )
            )
        }
        sendMessageToServer(trimmedText, isSystemInit = false)
    }

    private fun sendMessageToServer(text: String, isSystemInit: Boolean) {
        val currentData = _state.value.uiState.data ?: AiSpeakingUiData()

        viewModelScope.launch {
            sendAgentMessageUseCase(
                sessionId = currentData.currentSessionId,
                message = text,
                topicId = topicId,
                topicName = topicName,
                isNeedSuggestions = true
            ).onSuccess { aiMsg ->
                _state.update { state ->
                    val updatedData = (state.uiState.data ?: AiSpeakingUiData()).copy(
                        currentSessionId = aiMsg.sessionId.ifBlank { currentData.currentSessionId },
                        messages = (state.uiState.data?.messages.orEmpty()) + aiMsg,
                        suggestions = aiMsg.suggestions
                    )
                    state.copy(
                        isPremium = aiMsg.isPremium,
                        remainingMessages = aiMsg.remainingMessages,
                        maxCharLimit = aiMsg.activeCharLimit,
                        uiState = BaseUiState(data = updatedData, isLoading = false, error = null)
                    )
                }

                playAiResponse(aiMsg.text)

            }.onErrorDetails { err, serverMessage -> // Sử dụng onErrorDetails để bắt lỗi "Limited" chính xác từ Server
                _state.update { state ->
                    if (isSystemInit && currentData.messages.isEmpty()) {
                        state.copy(uiState = BaseUiState<AiSpeakingUiData>(isLoading = false, error = err.asUiText()))
                    } else {
                        state.copy(uiState = state.uiState.copy(isLoading = false))
                    }
                }

                val isLimitError = serverMessage == "Limited" ||
                        (err is AppError.Http && err.message == "Limited") ||
                        (err is AppError.Validation && err.reason == "Limited")

                if (isLimitError) {
                    _state.update { it.copy(isShowLimitConfirmDialog = true) }
                } else if (!isSystemInit || currentData.messages.isNotEmpty()) {
                    val toastText = if (!serverMessage.isNullOrBlank()) {
                        UiText.DynamicString(serverMessage)
                    } else {
                        err.asUiText()
                    }
                    sendGlobalEffect(BaseEffect.ShowToast(toastText))
                }
            }
        }
    }

    private fun playAiResponse(content: String) {
        textToSpeechParser.playAudio(
            url = null,
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