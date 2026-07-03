package com.codework.dailyenglish.presentation.screen.flashcard

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.utils.TextToSpeechUtils
import com.codework.dailyenglish.data.vocabulary.local.FlashcardDataStore
import com.codework.dailyenglish.presentation.screen.flashcard.mvi.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.dictionary.usecase.ToggleSaveWordUseCase

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val flashcardDataStore: FlashcardDataStore,
    private val toggleSaveWordUseCase: ToggleSaveWordUseCase,
    application: Application
) : BaseViewModel<FlashcardState, FlashcardEvent, FlashcardEffect>(FlashcardState()) {

    private val ttsUtils = TextToSpeechUtils(application)

    init {
        val inputWords = flashcardDataStore.words
        if (inputWords.isNotEmpty()) {
            _state.update {
                it.copy(
                    words = inputWords,
                    currentIndex = 0,
                    isFlipped = false,
                    isFinished = false,
                    forgotWords = emptyList(),
                    showReviewOptions = false
                )
            }
            updateProgressState()
            flashcardDataStore.clear()

            inputWords.firstOrNull()?.let { firstWord ->
                playAudio(firstWord.usAudioUrl, firstWord.word, 1f)
            }
        }
    }

    override fun onEvent(event: FlashcardEvent) {
        when (event) {
            is FlashcardEvent.OnCardClick -> {
                _state.update { it.copy(isFlipped = !it.isFlipped) }
            }

            is FlashcardEvent.OnToggleSaveClick -> {
                val currentWord = getCurrentWord()
                if (currentWord != null) {
                    _state.update { currentState ->
                        val updatedWords = currentState.words.map { word ->
                            if (word.id == currentWord.id) word.copy(isSaved = !word.isSaved) else word
                        }
                        currentState.copy(words = updatedWords)
                    }
                    viewModelScope.launch {
                        toggleSaveWordUseCase(currentWord.id).onError {}
                    }
                }
            }

            is FlashcardEvent.OnKnowItClick -> {
                moveToNextCard()
            }

            is FlashcardEvent.OnStillLearningClick -> {
                val currentState = _state.value
                val currentWord = getCurrentWord()

                if (currentWord != null) {
                    // Chỉ thêm vào danh sách hoãn học nếu từ này CHƯA TỒN TẠI trong forgotWords của hiệp hiện tại
                    val updatedForgot = currentState.forgotWords.toMutableList().apply {
                        if (none { it.id == currentWord.id }) {
                            add(currentWord)
                        }
                    }
                    _state.update { it.copy(forgotWords = updatedForgot) }
                }
                moveToNextCard()
            }

            is FlashcardEvent.OnReviewForgotWords -> {
                val currentState = _state.value
                if (currentState.forgotWords.isNotEmpty()) {
                    val nextRoundWords = currentState.forgotWords.toList() // Copy danh sách từ quên ra riêng

                    _state.update {
                        it.copy(
                            words = nextRoundWords,       // Hiệp mới chỉ chứa đúng X từ bị quên
                            currentIndex = 0,             // Reset về từ đầu tiên của hiệp mới
                            isFlipped = false,
                            showReviewOptions = false,
                            forgotWords = emptyList()     // XÓA TRẮNG forgotWords để tích lũy cho hiệp sau nữa nếu lại quên tiếp
                        )
                    }
                    updateProgressState() // Cập nhật lại thanh progress theo tổng số mới (Ví dụ: 0/3 thay vì bị cộng dồn)

                    nextRoundWords.firstOrNull()?.let { firstWord ->
                        playAudio(firstWord.usAudioUrl, firstWord.word, 1f)
                    }
                }
            }

            is FlashcardEvent.OnForceFinish -> {
                _state.update { it.copy(isFinished = true, showReviewOptions = false) }
            }

            is FlashcardEvent.OnPlayAudio -> {
                getCurrentWord()?.let {
                    playAudio(it.usAudioUrl, it.word, 1f)
                }
            }
        }
    }

    private fun moveToNextCard() {
        val currentState = _state.value
        val nextIndex = currentState.currentIndex + 1

        if (nextIndex < currentState.words.size) {
            _state.update { it.copy(currentIndex = nextIndex, isFlipped = false) }
            updateProgressState()
            getCurrentWord()?.let { nextWord ->
                playAudio(nextWord.usAudioUrl, nextWord.word, 1f)
            }
        } else {
            if (currentState.forgotWords.isNotEmpty()) {
                _state.update {
                    it.copy(
                        showReviewOptions = true,
                        progress = 1f,
                        progressText = "${currentState.words.size}/${currentState.words.size}"
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isFinished = true,
                        progress = 1f,
                        progressText = "${currentState.words.size}/${currentState.words.size}"
                    )
                }
            }
        }
    }

    private fun playAudio(url: String?, word: String, speed: Float) {
        ttsUtils.playAudio(
            url = url,
            textToSpeak = word,
            speed = speed,
            scope = viewModelScope
        )
    }

    private fun updateProgressState() {
        val currentState = _state.value
        if (currentState.words.isEmpty()) return
        val total = currentState.words.size
        val current = currentState.currentIndex
        _state.update { it.copy(progress = current.toFloat() / total.toFloat(), progressText = "$current/$total") }
    }

    private fun getCurrentWord(): Word? {
        return _state.value.words.getOrNull(_state.value.currentIndex)
    }

    override fun onCleared() {
        super.onCleared()
        ttsUtils.release()
    }
}