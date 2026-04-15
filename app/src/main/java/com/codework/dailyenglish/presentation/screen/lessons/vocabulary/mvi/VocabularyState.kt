package com.codework.dailyenglish.presentation.screen.lessons.vocabulary.mvi

import com.codework.dailyenglish.presentation.screen.lessons.vocabulary.model.RelatedWord

data class VocabularyState(
    val id: String = "",
    val word: String = "",
    val ukPhonetic: String = "",
    val usPhonetic: String = "",
    val meaning: String = "",
    val imageUrl: String = "",
    val level: String = "",
    val partOfSpeech: String = "",
    val grammar: String = "",
    val examples: List<String> = emptyList(),
    val relatedWords: List<RelatedWord> = emptyList(),
    val progressCurrent: Int = 0,
    val progressTotal: Int = 1,
    val hints: List<String> = emptyList(),
    val quizOptions: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isMeaningRevealed: Boolean = false,
    val isHintRevealed: Boolean = false
)