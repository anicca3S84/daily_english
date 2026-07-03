package com.codework.dailyenglish.presentation.screen.flashcard.mvi

import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.presentation.screen.flashcard.model.FlashcardUiModel

data class FlashcardState(
    val words: List<Word> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isFinished: Boolean = false,

    val forgotWords: List<Word> = emptyList(),
    val showReviewOptions: Boolean = false,

    val progress: Float = 0f,
    val progressText: String = ""
)