package com.codework.daily_english.presentation.screen.lessons.speaking.mvi

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