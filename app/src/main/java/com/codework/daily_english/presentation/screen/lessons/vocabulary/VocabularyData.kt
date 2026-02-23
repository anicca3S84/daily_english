package com.codework.daily_english.presentation.screen.lessons.vocabulary

import com.codework.daily_english.presentation.model.RelatedWord
data class VocabularyData(
    val id: String,
    val word: String,
    val ukPhonetic: String,
    val usPhonetic: String,
    val meaning: String,
    val imageUrl: String,
    val level: String,
    val partOfSpeech: String,
    val grammar: String,
    val examples: List<String>,
    val relatedWords: List<RelatedWord>,
    val progressCurrent: Int,
    val progressTotal: Int,
    val hints: List<String>,
    val quizOptions: List<String>,
    val correctAnswerIndex: Int,
    val selectedOptionIndex: Int? = null,

    val isMeaningRevealed: Boolean = false, // Trạng thái hiện giải thích chi tiết
    val isHintRevealed: Boolean = false     // THÊM: Trạng thái hiện gợi ý
)

