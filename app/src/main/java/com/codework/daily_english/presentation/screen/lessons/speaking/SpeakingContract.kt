package com.codework.daily_english.presentation.screen.lessons.speaking

import java.util.UUID

// --- DATA MODELS ---
enum class RecordState {
    READY_TO_START, RECORDING, PROCESSING, AI_SPEAKING, PAUSED, COMPLETED
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val translatedText: String? = null,
    val isTranslated: Boolean = false
)

// 1. STATE (Đổi SpeakingData thành SpeakingState)
data class SpeakingState(
    val missionContext: String = "At the coffee shop",
    val missionTarget: String = "Order a Cappuccino and ask for the price.",
    val messages: List<ChatMessage> = emptyList(),
    val recordState: RecordState = RecordState.READY_TO_START,
    val suggestedHints: List<String> = emptyList(),
    val isHintSheetOpen: Boolean = false,
    val progressCurrent: Int = 4,
    val progressTotal: Int = 5
)

// 2. EVENT
sealed interface SpeakingEvent {
    data object OnGoBack : SpeakingEvent
    data object OnStartRoleplay : SpeakingEvent
    data object OnToggleRecord : SpeakingEvent
    data object OnSwitchToTyping : SpeakingEvent
    data class OnPlayAudio(val messageId: String) : SpeakingEvent
    data class OnToggleTranslate(val messageId: String) : SpeakingEvent
    data class OnUserSpeechDone(val text: String) : SpeakingEvent
    data object OnAiSpeechDone : SpeakingEvent
    data object OnToggleHintSheet : SpeakingEvent
    data class OnUseHint(val text: String) : SpeakingEvent
    data object OnFinishLesson : SpeakingEvent
    data object OnRetry : SpeakingEvent
}

// 3. EFFECT
sealed interface SpeakingEffect {
    data object NavigateBack : SpeakingEffect
    data object NavigateToFinish : SpeakingEffect
    data class PlayTtsAudio(val text: String) : SpeakingEffect // Hiệu ứng đọc văn bản
    data class ShowToast(val message: String) : SpeakingEffect
}