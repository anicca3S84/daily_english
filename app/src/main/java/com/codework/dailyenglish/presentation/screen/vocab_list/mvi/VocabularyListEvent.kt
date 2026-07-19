package com.codework.dailyenglish.presentation.screen.vocab_list.mvi

import com.codework.dailyenglish.domain.dictionary.model.Word


sealed interface VocabularyListEvent {
    data class OnToggleSaveClick(val word: Word) : VocabularyListEvent
    data class OnPlayAudio(val word: Word) : VocabularyListEvent
    data class OnSelectWordToggle(val wordId: String) : VocabularyListEvent
    object OnSelectAllToggle : VocabularyListEvent
    object OnFlashCardClick : VocabularyListEvent
    object OnRefresh : VocabularyListEvent
    data class OnWordClick(val wordId: String) : VocabularyListEvent
}