package com.codework.dailyenglish.presentation.screen.translate

import android.media.MediaPlayer
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.base.BaseEffect.*
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.translate.usecase.ObserveTranslationHistoryUseCase
import com.codework.dailyenglish.domain.translate.usecase.SaveTranslationHistoryUseCase
import com.codework.dailyenglish.domain.translate.usecase.TranslateTextUseCase
import com.codework.dailyenglish.presentation.manager.TextToSpeechManager
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
import kotlinx.coroutines.flow.*


@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translateTextUseCase: TranslateTextUseCase,
    private val observeHistoryUseCase: ObserveTranslationHistoryUseCase,
    private val saveHistoryUseCase: SaveTranslationHistoryUseCase,
    private val ttsManager: TextToSpeechManager
) : BaseViewModel<TranslateState, TranslateEvent, TranslateEffect>(
    initialState = TranslateState()
) {

    private var translateJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    // Tạo luồng phát hiện thay đổi chữ nhập vào để thực hiện debounce
    private val sourceTextFlow = MutableStateFlow("")

    init {
        observeHistory()
        setupRealtimeTranslation()
    }

    @OptIn(FlowPreview::class)
    private fun setupRealtimeTranslation() {
        sourceTextFlow
            .debounce(800L) // Đợi người dùng dừng gõ phím 800ms
            .distinctUntilChanged()
            .onEach { text ->
                if (text.isBlank()) {
                    _state.update { it.copy(targetText = "", isTranslating = false) }
                    return@onEach
                }
                executeTranslation(text)
            }
            .launchIn(viewModelScope)
    }

    private fun executeTranslation(text: String) {
        translateJob?.cancel()
        translateJob = viewModelScope.launch {
            val currentState = _state.value
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
                        // Hiển thị loading "..." ngay khi người dùng đang gõ phím
                        isTranslating = event.text.isNotBlank(),
                        historyState = it.historyState.copy(error = null)
                    )
                }
                sourceTextFlow.value = event.text
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
                sourceTextFlow.value = ""
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
                sourceTextFlow.value = _state.value.sourceText
            }

            is TranslateEvent.OnPlaySourceAudio -> {
                playAudio(url = null, textToSpeak = _state.value.sourceText)
            }

            is TranslateEvent.OnPlayTargetAudio -> {
                playAudio(url = null, textToSpeak = _state.value.targetText)
            }

            is TranslateEvent.OnCameraClick -> {
                sendEffect(TranslateEffect.NavigateToCameraScanner)
            }

            is TranslateEvent.OnImageClick -> {
                sendEffect(TranslateEffect.NavigateToGalleryPicker)
            }

            is TranslateEvent.OnMicClick -> {
                // Mic Click sẽ được xử lý quyền trực tiếp từ UI Screen
            }

            is TranslateEvent.OnSeeAllClick -> {
                sendEffect(TranslateEffect.NavigateToHistory)
            }
        }
    }

    fun playAudio(url: String?, textToSpeak: String? = null, speed: Float = 1f) {
        if (url.isNullOrBlank()) {
            textToSpeak?.let { ttsManager.speak(it, speed) }
            return
        }
        viewModelScope.launch {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                } else {
                    mediaPlayer?.reset()
                }

                mediaPlayer?.apply {
                    setDataSource(url)
                    setOnPreparedListener { mp ->
                        try {
                            mp.playbackParams = mp.playbackParams.setSpeed(speed)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        mp.start()
                    }
                    setOnErrorListener { _, _, _ ->
                        textToSpeak?.let { ttsManager.speak(it, speed) }
                        true
                    }
                    prepareAsync()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                textToSpeak?.let { ttsManager.speak(it, speed) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
        ttsManager.shutDown()
    }
}