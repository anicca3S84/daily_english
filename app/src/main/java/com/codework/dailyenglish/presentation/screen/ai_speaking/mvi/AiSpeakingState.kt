package com.codework.dailyenglish.presentation.screen.ai_speaking.mvi

import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.model.ChatSession
import com.codework.dailyenglish.core.base.BaseUiState

data class AiSpeakingUiData(
    val currentSessionId: String = "new",
    val messages: List<ChatMessage> = emptyList(),
    val topicName: String = "",
    val suggestions: List<String> = emptyList()
)

data class AiSpeakingState(
    val uiState: BaseUiState<AiSpeakingUiData> = BaseUiState(),
    val isRecording: Boolean = false,
    val isSuggestionsVisible: Boolean = false,

    // BỔ SUNG LỊCH SỬ SESSIONS
    val historySessions: List<ChatSession> = emptyList(),

    val isPremium: Boolean = false,
    val remainingMessages: Int = 3,
    val maxCharLimit: Int = 150,
    val isShowLimitConfirmDialog: Boolean = false,
    val isShowPremiumUpgradeDialog: Boolean = false
)