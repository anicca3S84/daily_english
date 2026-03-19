package com.codework.daily_english.presentation.screen.lessons.speaking

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.core.base.BaseState
import com.codework.daily_english.presentation.screen.lessons.speaking.model.ChatMessage
import com.codework.daily_english.presentation.screen.lessons.speaking.model.RecordState
import com.codework.daily_english.presentation.screen.lessons.speaking.mvi.SpeakingEffect
import com.codework.daily_english.presentation.screen.lessons.speaking.mvi.SpeakingEvent
import com.codework.daily_english.presentation.screen.lessons.speaking.mvi.SpeakingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpeakingViewModel @Inject constructor() :
    BaseViewModel<SpeakingState, SpeakingEvent, SpeakingEffect>() {

    private var isManuallyPaused = false
    private var turnCount = 0

    init {
        loadInitialData()
    }

    override fun onEvent(event: SpeakingEvent) {
        val currentState = (_uiState.value as? BaseState.Success)?.data ?: return

        when (event) {
            is SpeakingEvent.OnGoBack -> sendEffect(SpeakingEffect.NavigateBack)
            is SpeakingEvent.OnFinishLesson -> sendEffect(SpeakingEffect.NavigateToFinish)
            is SpeakingEvent.OnStartRoleplay -> startRoleplay(currentState)
            is SpeakingEvent.OnToggleRecord -> handleManualMicToggle(currentState)
            is SpeakingEvent.OnToggleTranslate -> toggleTranslation(currentState, event.messageId)
            is SpeakingEvent.OnUserSpeechDone -> handleUserSpeechDone(currentState, event.text)
            is SpeakingEvent.OnAiSpeechDone -> handleAiSpeechDone(currentState)
            is SpeakingEvent.OnToggleHintSheet -> {
                _uiState.update { BaseState.Success(currentState.copy(isHintSheetOpen = !currentState.isHintSheetOpen)) }
            }
            is SpeakingEvent.OnUseHint -> {
                _uiState.update { BaseState.Success(currentState.copy(isHintSheetOpen = false)) }
                onEvent(SpeakingEvent.OnUserSpeechDone(event.text))
            }
            is SpeakingEvent.OnPlayAudio -> {
                val msg = currentState.messages.find { it.id == event.messageId }
                msg?.let { sendEffect(SpeakingEffect.PlayTtsAudio(it.text)) }
            }
            is SpeakingEvent.OnRetry -> loadInitialData()
            else -> {}
        }
    }

    private fun loadInitialData() {
        _uiState.update { BaseState.Success(SpeakingState(recordState = RecordState.READY_TO_START)) }
    }

    private fun startRoleplay(currentState: SpeakingState) {
        viewModelScope.launch {
            _uiState.update { BaseState.Success(currentState.copy(recordState = RecordState.PROCESSING)) }
            delay(800)

            val aiGreeting = ChatMessage(
                text = "Hello! Welcome to Star Coffee. What can I get for you today?",
                isFromUser = false,
                translatedText = "Xin chào! Chào mừng bạn đến với Star Coffee. Bạn muốn dùng gì hôm nay?"
            )

            _uiState.update { BaseState.Success(currentState.copy(
                messages = listOf(aiGreeting),
                recordState = RecordState.AI_SPEAKING,
                suggestedHints = listOf("I'd like a medium cappuccino, please.", "Can I get a hot coffee?")
            )) }

            sendEffect(SpeakingEffect.PlayTtsAudio(aiGreeting.text))
            delay(3000)
            onEvent(SpeakingEvent.OnAiSpeechDone)
        }
    }

    private fun handleAiSpeechDone(currentState: SpeakingState) {
        val nextState = when {
            currentState.recordState == RecordState.COMPLETED -> RecordState.COMPLETED
            isManuallyPaused -> RecordState.PAUSED
            else -> RecordState.RECORDING
        }
        _uiState.update { BaseState.Success(currentState.copy(recordState = nextState)) }
    }

    private fun handleUserSpeechDone(currentState: SpeakingState, text: String) {
        turnCount++
        viewModelScope.launch {
            val userMsg = ChatMessage(text = text, isFromUser = true)
            _uiState.update { BaseState.Success(currentState.copy(
                messages = currentState.messages + userMsg,
                recordState = RecordState.PROCESSING
            )) }

            delay(1500)

            val aiResponse = if (turnCount == 1) {
                ChatMessage(text = "Sure thing! Hot or iced?", isFromUser = false, translatedText = "Chắc chắn rồi! Bạn dùng nóng hay đá?")
            } else {
                ChatMessage(text = "Here is your hot cappuccino. That will be 4 dollars. Enjoy!", isFromUser = false, translatedText = "Đây là ly cappuccino nóng của bạn. Hết 4 đô la ạ!")
            }

            val isLast = turnCount > 1
            _uiState.update { BaseState.Success(currentState.copy(
                messages = currentState.messages + userMsg + aiResponse,
                recordState = RecordState.AI_SPEAKING,
                suggestedHints = if (isLast) emptyList() else listOf("Hot, please.", "Iced.")
            )) }

            sendEffect(SpeakingEffect.PlayTtsAudio(aiResponse.text))
            delay(2500)

            if (isLast) {
                _uiState.update { BaseState.Success((_uiState.value as BaseState.Success).data.copy(recordState = RecordState.COMPLETED)) }
            }
            onEvent(SpeakingEvent.OnAiSpeechDone)
        }
    }

    private fun handleManualMicToggle(currentState: SpeakingState) {
        // Giả lập logic ghi âm
        if (currentState.recordState == RecordState.RECORDING) {
            isManuallyPaused = true
            onEvent(SpeakingEvent.OnUserSpeechDone(if(turnCount == 0) "I'd like a cappuccino." else "Hot, please."))
        } else if (currentState.recordState == RecordState.PAUSED) {
            isManuallyPaused = false
            _uiState.update { BaseState.Success(currentState.copy(recordState = RecordState.RECORDING)) }
        }
    }

    private fun toggleTranslation(currentState: SpeakingState, messageId: String) {
        val updatedMessages = currentState.messages.map {
            if (it.id == messageId && it.translatedText != null) it.copy(isTranslated = !it.isTranslated) else it
        }
        _uiState.update { BaseState.Success(currentState.copy(messages = updatedMessages)) }
    }
}