package com.codework.dailyenglish.presentation.screen.ai_speaking.mvi

import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.core.base.BaseUiState // Import class bọc generic của bạn

/**
 * Đối tượng chứa dữ liệu thực tế khi tải thành công (Success Data)
 */
data class AiSpeakingUiData(
    val currentSessionId: String = "new",
    val messages: List<ChatMessage> = emptyList(),
    val topicName: String = "",
    val suggestions: List<String> = emptyList()
)

/**
 * MVI State cho màn hình Speaking.
 * KHÔNG kế thừa từ BaseUiState, xóa hoàn toàn dấu hai chấm đằng sau đi!
 */
data class AiSpeakingState(
    // Khai báo uiState là một biến thành phần (Property) bên trong, truyền đúng kiểu AiSpeakingUiData vào
    val uiState: BaseUiState<AiSpeakingUiData> = BaseUiState(),
    val isRecording: Boolean = false,
    val isSuggestionsVisible: Boolean = false
)