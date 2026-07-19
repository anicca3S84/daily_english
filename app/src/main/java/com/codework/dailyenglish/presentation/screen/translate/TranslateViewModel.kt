package com.codework.dailyenglish.presentation.screen.translate

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.base.BaseEffect.*
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.core.utils.TextToSpeechParser
import com.codework.dailyenglish.core.utils.VoiceToTextParser
import com.codework.dailyenglish.domain.dictionary.usecase.ClearTranslationHistoryUseCase
import com.codework.dailyenglish.domain.translate.usecase.ObserveTranslationHistoryUseCase
import com.codework.dailyenglish.domain.translate.usecase.SaveTranslationHistoryUseCase
import com.codework.dailyenglish.domain.translate.usecase.TranslateTextUseCase
import com.codework.dailyenglish.presentation.screen.translate.mapper.toUiModels
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateEffect
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateEvent
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateState
import com.codework.dailyenglish.presentation.utils.UiText
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import android.util.Log

private data class TranslationRequest(
    val text: String,
    val shouldTranslate: Boolean = true
)

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translateTextUseCase: TranslateTextUseCase,
    private val observeHistoryUseCase: ObserveTranslationHistoryUseCase,
    private val saveHistoryUseCase: SaveTranslationHistoryUseCase,
    private val clearTranslationHistoryUseCase: ClearTranslationHistoryUseCase,
    private val textToSpeechParser: TextToSpeechParser,
    private val voiceToTextParser: VoiceToTextParser,
) : BaseViewModel<TranslateState, TranslateEvent, TranslateEffect>(
    initialState = TranslateState()
) {

    private var translateJob: Job? = null
    private val sourceTextFlow = MutableStateFlow(TranslationRequest(""))

    init {
        observeHistory()
        setupRealtimeTranslation()
        observeVoiceState()
    }

    @OptIn(FlowPreview::class)
    private fun setupRealtimeTranslation() {
        sourceTextFlow
            .debounce(800L)
            .distinctUntilChanged()
            .onEach { request ->
                if (!request.shouldTranslate) {
                    return@onEach
                }

                val text = request.text
                if (text.isBlank()) {
                    _state.update { it.copy(targetText = "", isTranslating = false) }
                    return@onEach
                }
                executeTranslation(text)
            }
            .launchIn(viewModelScope)
    }

    private fun observeVoiceState() {
        voiceToTextParser.state
            .onEach { voiceState ->
                _state.update {
                    it.copy(
                        isRecording = voiceState.isListening,
                        sourceText = voiceState.spokenText.ifBlank { it.sourceText },
                        isTranslating = voiceState.spokenText.isNotBlank() && !voiceState.isListening
                    )
                }

                if (voiceState.spokenText.isNotBlank() && !voiceState.isListening) {
                    sourceTextFlow.value = TranslationRequest(voiceState.spokenText, shouldTranslate = true)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun executeTranslation(text: String) {
        translateJob?.cancel()
        translateJob = viewModelScope.launch {
            val currentState = _state.value
            _state.update { it.copy(isTranslating = true) }

            try {
                translateTextUseCase(
                    text = text,
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
            } catch (e: Exception) {
                // SỬA LỖI: Bẫy mọi ngoại lệ mạng/phân tích dữ liệu để đảm bảo tắt Translating an toàn
                Log.e("TranslateViewModel", "Lỗi trong quá trình dịch thuật: ${e.message}", e)
                _state.update {
                    it.copy(
                        isTranslating = false
                    )
                }
            }
        }
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
                        isTranslating = event.text.isNotBlank(),
                        historyState = it.historyState.copy(error = null)
                    )
                }
                sourceTextFlow.value = TranslationRequest(event.text, shouldTranslate = true)
            }

            is TranslateEvent.OnClearText -> {
                _state.update {
                    it.copy(
                        sourceText = "",
                        targetText = "",
                        isTranslating = false,
                        historyState = it.historyState.copy(error = null)
                    )
                }
                sourceTextFlow.value = TranslationRequest("", shouldTranslate = false)
            }

            is TranslateEvent.OnSwapLanguage -> {
                _state.update {
                    it.copy(
                        sourceLang = it.targetLang,
                        targetLang = it.sourceLang,
                        sourceText = it.targetText,
                        targetText = it.sourceText,
                        isTranslating = it.targetText.isNotBlank(),
                        historyState = it.historyState.copy(error = null)
                    )
                }
                sourceTextFlow.value = TranslationRequest(_state.value.sourceText, shouldTranslate = true)
            }

            is TranslateEvent.OnPlaySourceAudio -> {
                val currentLang = _state.value.sourceLang
                val targetLocale = getLocaleFromLangCode(currentLang)

                playAudio(
                    url = null,
                    word = _state.value.sourceText,
                    locale = targetLocale
                )
            }

            is TranslateEvent.OnPlayTargetAudio -> {
                val currentLang = _state.value.targetLang
                val targetLocale = getLocaleFromLangCode(currentLang)

                playAudio(
                    url = null,
                    word = _state.value.targetText,
                    locale = targetLocale
                )
            }

            is TranslateEvent.OnCameraClick -> {
                sendEffect(TranslateEffect.NavigateToCameraScanner)
            }

            is TranslateEvent.OnImageClick -> {
                sendEffect(TranslateEffect.NavigateToGalleryPicker)
            }

            is TranslateEvent.OnMicClick -> {
                if (voiceToTextParser.state.value.isListening) {
                    voiceToTextParser.stopListening()

                    // SỬA LỖI: Dịch ngay văn bản thô đang hiển thị khi bấm dừng thủ công
                    val currentText = _state.value.sourceText
                    if (currentText.isNotBlank()) {
                        _state.update {
                            it.copy(
                                isRecording = false,
                                isTranslating = true
                            )
                        }
                        sourceTextFlow.value = TranslationRequest(currentText, shouldTranslate = true)
                    }
                } else {
                    _state.update {
                        it.copy(
                            sourceText = "",
                            targetText = "",
                            isTranslating = false
                        )
                    }
                    sourceTextFlow.value = TranslationRequest("", shouldTranslate = false)
                    val langCode = getLocaleFromLangCode(_state.value.sourceLang).toLanguageTag()

                    voiceToTextParser.startListening(
                        context = event.context,
                        languageCode = langCode,
                        minDurationMillis = 1500L,
                        completeSilenceMillis = 1200L,
                        possiblyCompleteSilenceMillis = 1000L
                    )
                }
            }

            is TranslateEvent.OnClearAllClick -> {
                viewModelScope.launch {
                    clearTranslationHistoryUseCase()
                        .onSuccess {
                            sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_success)))
                        }
                        .onError {
                            sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_errorr)))
                        }
                }
            }

            is TranslateEvent.OnCopySuccess -> {
                sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_copy_success)))
            }

            is TranslateEvent.OnHistoryItemClick -> {
                _state.update {
                    it.copy(
                        sourceText = event.source,
                        targetText = event.target,
                        isTranslating = false
                    )
                }
                sourceTextFlow.value = TranslationRequest(event.source, shouldTranslate = false)
            }

            is TranslateEvent.OnRefreshHistory -> {observeHistory()}

            else -> {}
        }
    }

    private fun getLocaleFromLangCode(langCode: String): Locale {
        return when (langCode.lowercase()) {
            "vi", "việt nam", "vietnamese" -> Locale("vi", "VN")
            "en", "anh", "english" -> Locale.US
            else -> Locale.getDefault()
        }
    }

    private fun playAudio(url: String?, word: String, locale: Locale) {
        textToSpeechParser.playAudio(
            url = url,
            textToSpeak = word,
            speed = 1f,
            locale = locale,
            scope = viewModelScope
        )
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeechParser.release()
        voiceToTextParser.release()
    }
}