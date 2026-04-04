package com.codework.daily_english.presentation.screen.translate

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.R
import com.codework.daily_english.core.base.BaseEffect.*
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.core.network.onError
import com.codework.daily_english.core.network.onSuccess
import com.codework.daily_english.domain.translate.usecase.ObserveTranslationHistoryUseCase
import com.codework.daily_english.domain.translate.usecase.SaveTranslationHistoryUseCase
import com.codework.daily_english.domain.translate.usecase.TranslateTextUseCase
import com.codework.daily_english.presentation.screen.translate.mapper.toUiModels
import com.codework.daily_english.presentation.screen.translate.mvi.TranslateEffect
import com.codework.daily_english.presentation.screen.translate.mvi.TranslateEvent
import com.codework.daily_english.presentation.screen.translate.mvi.TranslateState
import com.codework.daily_english.presentation.utils.UiText
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
    private val saveHistoryUseCase: SaveTranslationHistoryUseCase
) : BaseViewModel<TranslateState, TranslateEvent, TranslateEffect>(
    initialState = TranslateState()
) {

    private var translateJob: Job? = null

    init {
        observeHistory()
    }

    private fun observeHistory() {
        _state.update { it.copy(historyState = it.historyState.copy(isLoading = true)) }

        observeHistoryUseCase()
            .onEach { domainList ->
                val uiList = domainList.toUiModels()
                _state.update {
                    it.copy(
                        historyState = it.historyState.copy(
                            data = uiList,
                            isLoading = false,
                            error = null
                        )
                    )
                }
            }
            .catch {
                _state.update {
                    it.copy(
                        historyState = it.historyState.copy(
                            isLoading = false,
                            error = UiText.StringResource(R.string.error_could_not_load_history)
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.OnTextChange -> {
                _state.update {
                    it.copy(
                        sourceText = event.text,
                        historyState = it.historyState.copy(error = null)
                    )
                }
            }

            is TranslateEvent.OnClearText -> {
                _state.update {
                    it.copy(
                        sourceText = "",
                        targetText = "",
                        historyState = it.historyState.copy(error = null)
                    )
                }
            }

            is TranslateEvent.OnSwapLanguage -> {
                _state.update {
                    it.copy(
                        sourceLang = it.targetLang,
                        targetLang = it.sourceLang,
                        sourceText = it.targetText,
                        targetText = it.sourceText,
                        historyState = it.historyState.copy(error = null)
                    )
                }
            }

            is TranslateEvent.OnTranslateClick -> {
                if (_state.value.isTranslating) return
                translateJob?.cancel()

                translateJob = viewModelScope.launch {
                    val currentState = _state.value
                    if (currentState.sourceText.isBlank()) return@launch

                    _state.update {
                        it.copy(
                            isTranslating = true,
                            historyState = it.historyState.copy(error = null)
                        )
                    }

                    translateTextUseCase(
                        text = currentState.sourceText,
                        sourceLang = currentState.sourceLang,
                        targetLang = currentState.targetLang
                    )
                        .onSuccess { result ->
                            _state.update {
                                it.copy(
                                    targetText = result.translatedText,
                                    isTranslating = false
                                )
                            }

                            result.historyItem?.let { item ->
                                launch {
                                    runCatching { saveHistoryUseCase(item) }
                                }
                            }
                        }
                        .onError { appError ->
                            _state.update {
                                it.copy(
                                    isTranslating = false,
                                    historyState = it.historyState.copy(error = appError.asUiText())
                                )
                            }
                        }
                }
            }

            is TranslateEvent.OnCameraClick -> sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_coming_soon)))
            is TranslateEvent.OnImageClick -> sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_coming_soon)))
            is TranslateEvent.OnMicClick -> sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_coming_soon)))
            TranslateEvent.OnSeeAllClick -> sendEffect(TranslateEffect.NavigateToHistory)
        }
    }
}