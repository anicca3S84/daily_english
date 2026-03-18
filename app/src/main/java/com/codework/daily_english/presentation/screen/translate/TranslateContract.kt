package com.codework.daily_english.presentation.screen.translate

// 1. STATE (Đổi tên TranslateData thành TranslateState)
data class TranslateState(
    val sourceText: String = "",
    val targetText: String = "",
    val sourceLang: String = "English",
    val targetLang: String = "Vietnamese",
    val isTranslating: Boolean = false
)

// 2. EVENT
sealed interface TranslateEvent {
    data class OnTextChange(val text: String) : TranslateEvent
    data object OnTranslateClick : TranslateEvent
    data object OnSwapLanguage : TranslateEvent
    data object OnClearText : TranslateEvent

    // Các Event bổ sung cho các nút trong UI của bạn
    data object OnCameraClick : TranslateEvent
    data object OnImageClick : TranslateEvent
    data object OnMicClick : TranslateEvent
}

// 3. EFFECT
sealed interface TranslateEffect {
    data class ShowToast(val message: String) : TranslateEffect
    // Gợi ý cho tương lai: data object OpenCamera : TranslateEffect
}