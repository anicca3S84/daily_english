package com.codework.dailyenglish.presentation.screen.lessons.review.mvi

import com.codework.dailyenglish.presentation.screen.lessons.review.model.QuizType

data class QuizReviewState(
    val id: String = "",
    val sentence: String = "",
    val translatedSentence: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerRevealed: Boolean = false,
    val progressCurrent: Int = 0,
    val progressTotal: Int = 1,
    val type: QuizType = QuizType.VOCABULARY
)