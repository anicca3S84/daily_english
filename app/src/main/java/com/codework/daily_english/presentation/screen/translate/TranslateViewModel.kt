package com.codework.daily_english.presentation.screen.translate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState // Import Sealed Class UiState của bạn
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TranslateViewModel @Inject constructor() : BaseViewModel<TranslateData>() {

    fun onEvent(event: TranslateEvent) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        when(event) {
            is TranslateEvent.OnTextChange -> {
                _uiState.value = UiState.Success(currentState.copy(sourceText = event.text))
            }
            is TranslateEvent.OnClearText -> {
                _uiState.value = UiState.Success(currentState.copy(sourceText = "", targetText = ""))
            }
            is TranslateEvent.OnSwapLanguage -> {
                _uiState.value = UiState.Success(
                    currentState.copy(
                        sourceLang = currentState.targetLang,
                        targetLang = currentState.sourceLang,
                        sourceText = currentState.targetText, // Đảo cả văn bản
                        targetText = currentState.sourceText
                    )
                )
            }
            is TranslateEvent.OnTranslateClick -> {
                if (currentState.sourceText.isBlank()) return
                viewModelScope.launch {
                    _uiState.value = UiState.Success(currentState.copy(isTranslating = true))
                    delay(1000) // Giả lập gọi API
                    // TODO: Gọi Google Translate API hoặc Gemini API ở đây
                    val fakeResult = "Đây là bản dịch giả lập của: ${currentState.sourceText}"
                    _uiState.value = UiState.Success(
                        currentState.copy(
                            targetText = fakeResult,
                            isTranslating = false
                        )
                    )
                }
            }
        }
    }
}