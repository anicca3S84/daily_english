package com.codework.dailyenglish.presentation.screen.lessons.speaking.mvi

import com.codework.dailyenglish.presentation.screen.lessons.speaking.model.ChatMessage
import com.codework.dailyenglish.presentation.screen.lessons.speaking.model.RecordState

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