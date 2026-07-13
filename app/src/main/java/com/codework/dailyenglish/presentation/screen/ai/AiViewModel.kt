package com.codework.dailyenglish.presentation.screen.ai

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onErrorDetails
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.core.utils.VoiceToTextParser
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.usecase.GetHistorySessionsUseCase
import com.codework.dailyenglish.domain.ai.usecase.GetSessionMessagesUseCase
import com.codework.dailyenglish.domain.ai.usecase.SendMessageUseCase
import com.codework.dailyenglish.domain.auth.usecase.GetMyProfileUseCase
import com.codework.dailyenglish.domain.user.usecase.GetUserInfoUseCase
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEffect
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEvent
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiState
import com.codework.dailyenglish.presentation.utils.UiText
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getHistorySessionsUseCase: GetHistorySessionsUseCase,
    private val voiceToTextParser: VoiceToTextParser,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getSessionMessagesUseCase: GetSessionMessagesUseCase,
) : BaseViewModel<AiState, AiEvent, AiEffect>(AiState()) {

    init {
        loadHistory()
        checkUserPremiumStatus()
        observeVoiceInput()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            getHistorySessionsUseCase()
                .onSuccess { sessions ->
                    _state.update { it.copy(historySessions = sessions) }
                }
                .onError { err ->
                    sendGlobalEffect(BaseEffect.ShowToast(err.asUiText()))
                }
        }
    }

    private fun checkUserPremiumStatus() {
        viewModelScope.launch {
            val localUser = getUserInfoUseCase()
            val initialPremium = localUser?.isPremium ?: false
            _state.update {
                it.copy(
                    isPremium = initialPremium,
                    remainingMessages = if (initialPremium) 70 else 10,
                    maxCharLimit = if (initialPremium) 300 else 150
                )
            }

            getMyProfileUseCase().onSuccess { freshUser ->
                _state.update {
                    it.copy(
                        isPremium = freshUser.isPremium,
                        remainingMessages = if (freshUser.isPremium) Int.MAX_VALUE else _state.value.remainingMessages,
                        maxCharLimit = if (freshUser.isPremium) 1000 else _state.value.maxCharLimit
                    )
                }
            }
        }
    }

    private fun sendMessage(text: String) {
        val trimmedText = text.trim()
        if (trimmedText.isBlank()) return

        val currentState = _state.value

        if (trimmedText.length > currentState.maxCharLimit) {
            sendGlobalEffect(BaseEffect.ShowToast(UiText.DynamicString("Tin nhắn không được quá ${currentState.maxCharLimit} ký tự.")))
            return
        }

        if (!currentState.isPremium && currentState.remainingMessages <= 0) {
            _state.update { it.copy(isShowPremiumUpgradeDialog = true) }
            return
        }

        val sessionId = currentState.currentSessionId ?: "new"
        val userMsg = ChatMessage(text = trimmedText, isUser = true)

        _state.update { it.copy(messages = it.messages + userMsg, isTyping = true, voiceCapturedText = "") }

        viewModelScope.launch {
            sendMessageUseCase(
                sessionId = sessionId,
                message = trimmedText,
                isNeedSuggestions = false
            ).onSuccess { chatMessage ->
                _state.update {
                    it.copy(
                        messages = it.messages + chatMessage,
                        isTyping = false,
                        isPremium = chatMessage.isPremium,
                        remainingMessages = chatMessage.remainingMessages,
                        maxCharLimit = chatMessage.activeCharLimit,
                        currentSessionId = chatMessage.sessionId
                    )
                }

                // SỬA ĐIỂM 1: Nếu tin nhắn được gửi đi từ một phòng chat mới ("new"),
                // chúng ta tiến hành nạp lại lịch sử ngay sau khi nhận phản hồi thành công
                // để cuộc trò chuyện mới này xuất hiện trong Drawer.
                if (sessionId == "new") {
                    loadHistory()
                }

            }.onErrorDetails { err, serverMessage ->
                _state.update { it.copy(isTyping = false) }
                val toastText = if (!serverMessage.isNullOrBlank()) {
                    UiText.DynamicString(serverMessage)
                } else {
                    err.asUiText()
                }
                sendGlobalEffect(BaseEffect.ShowToast(toastText))
            }
        }
    }

    override fun onEvent(event: AiEvent) {
        when (event) {
            is AiEvent.OnSendMessage -> sendMessage(event.message)
            is AiEvent.OnSuggestionClick -> sendMessage(event.text)

            // SỬA ĐIỂM 2: Khi nhấn tạo cuộc trò chuyện mới từ Drawer,
            // dọn dẹp màn hình hiện tại và đồng thời gọi lại loadHistory() để đồng bộ danh sách.
            AiEvent.OnNewChat -> {
                _state.update { it.copy(messages = emptyList(), currentSessionId = null) }
                loadHistory()
            }

            is AiEvent.OnLoadSession -> {
                _state.update { it.copy(currentSessionId = event.sessionId, isTyping = true) }
                viewModelScope.launch {
                    getSessionMessagesUseCase(event.sessionId)
                        .onSuccess { oldMessages ->
                            _state.update {
                                it.copy(
                                    messages = oldMessages,
                                    isTyping = false
                                )
                            }
                        }
                        .onError { err ->
                            _state.update { it.copy(isTyping = false) }
                            sendGlobalEffect(BaseEffect.ShowToast(err.asUiText()))
                        }
                }
            }
            is AiEvent.OnImageClick -> sendEffect(AiEffect.OpenGallery)
            is AiEvent.OnMicToggle -> toggleRecording(event.context)
            is AiEvent.OnDismissSubscriptionPopup -> {
                _state.update { it.copy(isShowPremiumUpgradeDialog = false) }
                checkUserPremiumStatus()
            }
            else -> {}
        }
    }

    private fun observeVoiceInput() {
        viewModelScope.launch {
            voiceToTextParser.state.collectLatest { voiceState ->
                if (_state.value.isVoiceRecording != voiceState.isListening) {
                    _state.update { it.copy(isVoiceRecording = voiceState.isListening) }
                }

                if (voiceState.spokenText.isNotBlank()) {
                    _state.update { it.copy(voiceCapturedText = voiceState.spokenText) }
                }
                if (!voiceState.isListening &&
                    voiceState.error == null &&
                    voiceState.spokenText.isNotBlank() &&
                    _state.value.voiceCapturedText == voiceState.spokenText
                ) {
                    val finalSpeechText = voiceState.spokenText
                    _state.update { it.copy(voiceCapturedText = "") }
                    sendMessage(finalSpeechText)
                    voiceToTextParser.stopListening()
                }

                voiceState.error?.let { err ->
                    sendGlobalEffect(BaseEffect.ShowToast(err))
                }
            }
        }
    }

    private fun toggleRecording(context: Context) {
        val nextRecordingState = !_state.value.isVoiceRecording
        if (nextRecordingState) {
            voiceToTextParser.startListening(context, languageCode = "en-US")
        } else {
            voiceToTextParser.stopListening()
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceToTextParser.release()
    }
}