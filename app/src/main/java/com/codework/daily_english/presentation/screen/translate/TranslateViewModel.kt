package com.codework.daily_english.presentation.screen.translate

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.core.result.onError
import com.codework.daily_english.core.result.onSuccess
import com.codework.daily_english.domain.translate.usecase.ObserveTranslationHistoryUseCase
import com.codework.daily_english.domain.translate.usecase.SaveTranslationHistoryUseCase
import com.codework.daily_english.domain.translate.usecase.TranslateTextUseCase
import com.codework.daily_english.presentation.screen.translate.mapper.toUiModels
import com.codework.daily_english.presentation.screen.translate.mvi.TranslateEffect
import com.codework.daily_english.presentation.screen.translate.mvi.TranslateEvent
import com.codework.daily_english.presentation.screen.translate.mvi.TranslateState
import com.codework.daily_english.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translateTextUseCase: TranslateTextUseCase,
    private val observeHistoryUseCase: ObserveTranslationHistoryUseCase,
    private val saveHistoryUseCase: SaveTranslationHistoryUseCase // ✅ Dùng UseCase thay vì Repository
) : BaseViewModel<TranslateState, TranslateEvent, TranslateEffect>(
    initialState = TranslateState()
) {

    private var translateJob: Job? = null

    init {
        // Khởi chạy lắng nghe lịch sử ngay khi App mở màn hình này
        observeHistory()
    }

    private fun observeHistory() {
        observeHistoryUseCase()
            .onEach { domainList ->
                // Map Domain Model -> UI Model trước khi đẩy vào State
                val uiList = domainList.toUiModels()
                _state.update { it.copy(history = uiList) }
            }
            .catch {
                _state.update { it.copy(error = "Không thể tải lịch sử dịch") }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.OnTextChange -> {
                _state.update {
                    it.copy(
                        sourceText = event.text,
                        error = null // Xóa lỗi cũ khi người dùng gõ lại
                    )
                }
            }

            is TranslateEvent.OnClearText -> {
                _state.update { it.copy(sourceText = "", targetText = "", error = null) }
            }

            is TranslateEvent.OnSwapLanguage -> {
                _state.update {
                    it.copy(
                        sourceLang = it.targetLang,
                        targetLang = it.sourceLang,
                        sourceText = it.targetText,
                        targetText = it.sourceText,
                        error = null
                    )
                }
            }

            is TranslateEvent.OnTranslateClick -> {
                // 1. Chống Double Click / Spam
                if (_state.value.isTranslating) return

                // 2. Hủy tác vụ cũ nếu đang chạy
                translateJob?.cancel()

                translateJob = viewModelScope.launch {
                    // 3. Lấy Fresh State ngay trong Coroutine để tránh stale data
                    val currentState = _state.value

                    if (currentState.sourceText.isBlank()) return@launch

                    _state.update { it.copy(isTranslating = true, error = null) }

                    // 4. Gọi UseCase thực hiện logic dịch
                    translateTextUseCase(
                        text = currentState.sourceText,
                        sourceLang = currentState.sourceLang,
                        targetLang = currentState.targetLang
                    )
                        .onSuccess { result ->
                            // Cập nhật kết quả dịch lên UI
                            _state.update {
                                it.copy(
                                    targetText = result.translatedText,
                                    isTranslating = false
                                )
                            }

                            // 5. Lưu lịch sử (Fire-and-forget) trong scope của ViewModel
                            result.historyItem?.let { item ->
                                launch {
                                    runCatching { saveHistoryUseCase(item) }
                                }
                            }
                        }
                        .onError { appError ->
                            // Map lỗi kỹ thuật sang ngôn ngữ người dùng
                            _state.update {
                                it.copy(
                                    isTranslating = false,
                                    error = appError.asUiText()
                                )
                            }
                        }
                }
            }

            is TranslateEvent.OnCameraClick -> sendEffect(TranslateEffect.ShowToast("Camera feature coming soon"))
            is TranslateEvent.OnImageClick -> sendEffect(TranslateEffect.ShowToast("Gallery feature coming soon"))
            is TranslateEvent.OnMicClick -> sendEffect(TranslateEffect.ShowToast("Voice input coming soon"))
        }
    }
}