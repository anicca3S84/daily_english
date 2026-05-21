package com.codework.dailyenglish.presentation.screen.saved_word.mvi

import com.codework.dailyenglish.domain.dictionary.model.Word


sealed interface SavedWordsEvent {
    data class OnRemoveClick(val word: Word) : SavedWordsEvent
    data class OnPlayAudio(val word: Word) : SavedWordsEvent
    data object OnFlashCardClick : SavedWordsEvent
}