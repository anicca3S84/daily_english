package com.codework.dailyenglish.presentation.screen.flashcard

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
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
    private val toggleSaveWordUseCase: ToggleSaveWordUseCase
) : BaseViewModel<FlashcardState, FlashcardEvent, FlashcardEffect>(FlashcardState()) {

    init {
        val inputWords = flashcardDataStore.words
        if (inputWords.isNotEmpty()) {
            _state.update { it.copy(words = inputWords, currentIndex = 0, isFlipped = false, isFinished = false) }
            updateProgressState()
            flashcardDataStore.clear()
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
                    // 1. Cập nhật trạng thái lưu trong List cục bộ ngay lập tức
                    _state.update { currentState ->
                        val updatedWords = currentState.words.map { word ->
                            if (word.id == currentWord.id) word.copy(isSaved = !word.isSaved) else word
                        }
                        currentState.copy(words = updatedWords)
                    }

                    viewModelScope.launch {
                        toggleSaveWordUseCase(currentWord.id).onError {
                        }
                    }
                }
            }

            is FlashcardEvent.OnKnowItClick -> {
                moveToNextCard()
            }

            is FlashcardEvent.OnPlayAudio -> { /* Logic play audio */ }
            is FlashcardEvent.OnStillLearningClick -> {
                _state.update { currentState ->
                    val currentList = currentState.words
                    val currentWord = currentList.getOrNull(currentState.currentIndex)

                    if (currentWord != null) {
                        // Đẩy từ hiện tại xuống cuối danh sách để bắt buộc học lại ở cuối phiên
                        val updatedList = currentList.toMutableList().apply {
                            add(currentWord)
                        }

                        val nextIndex = currentState.currentIndex + 1
                        val total = updatedList.size

                        currentState.copy(
                            words = updatedList,
                            currentIndex = nextIndex,
                            isFlipped = false,
                            progress = nextIndex.toFloat() / total.toFloat(),
                            progressText = "$nextIndex/$total"
                        )
                    } else {
                        currentState.copy(isFinished = true)
                    }
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
        } else {
            _state.update { it.copy(isFinished = true, progress = 1f, progressText = "${currentState.words.size}/${currentState.words.size}") }
        }
    }

    private fun updateProgressState() {
        val currentState = _state.value
        if (currentState.words.isEmpty()) return
        val total = currentState.words.size
        val current = currentState.currentIndex
        _state.update { it.copy(progress = current.toFloat() / total.toFloat(), progressText = "$current/$total") }
    }

    private fun getCurrentWord(): com.codework.dailyenglish.domain.dictionary.model.Word? {
        return _state.value.words.getOrNull(_state.value.currentIndex)
    }
}