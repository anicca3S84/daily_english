package com.codework.daily_english.presentation.screen.translate

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor() : BaseViewModel<TranslateState, TranslateEvent, TranslateEffect>() {

    init {
        // Khởi tạo ngay trạng thái Success với data rỗng ban đầu
        _uiState.update { UiState.Success(TranslateState()) }
    }

    override fun onEvent(event: TranslateEvent) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        when(event) {
            is TranslateEvent.OnTextChange -> {
                _uiState.update { UiState.Success(currentState.copy(sourceText = event.text)) }
            }

            is TranslateEvent.OnClearText -> {
                _uiState.update { UiState.Success(currentState.copy(sourceText = "", targetText = "")) }
            }

            is TranslateEvent.OnSwapLanguage -> {
                _uiState.update {
                    UiState.Success(
                        currentState.copy(
                            sourceLang = currentState.targetLang,
                            targetLang = currentState.sourceLang,
                            sourceText = currentState.targetText, // Đảo cả văn bản
                            targetText = currentState.sourceText
                        )
                    )
                }
            }

            is TranslateEvent.OnTranslateClick -> {
                if (currentState.sourceText.isBlank()) return

                viewModelScope.launch {
                    // Cập nhật trạng thái đang dịch (để hiện loading nhỏ ở thẻ kết quả)
                    _uiState.update { UiState.Success(currentState.copy(isTranslating = true)) }

                    delay(1000) // Giả lập gọi API

                    // TODO: Gọi Google Translate API hoặc Gemini API ở đây
                    val fakeResult = "Đây là bản dịch giả lập của: ${currentState.sourceText}"

                    _uiState.update {
                        UiState.Success(
                            currentState.copy(
                                targetText = fakeResult,
                                isTranslating = false
                            )
                        )
                    }
                }
            }

            // Xử lý các nút bấm mới
            is TranslateEvent.OnCameraClick -> sendEffect(TranslateEffect.ShowToast("Camera feature coming soon"))
            is TranslateEvent.OnImageClick -> sendEffect(TranslateEffect.ShowToast("Gallery feature coming soon"))
            is TranslateEvent.OnMicClick -> sendEffect(TranslateEffect.ShowToast("Voice input coming soon"))
        }
    }
}