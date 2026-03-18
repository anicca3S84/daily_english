package com.codework.daily_english.presentation.screen.lessons.vocabulary

import com.codework.daily_english.presentation.components.AudioRegion
import com.codework.daily_english.presentation.model.RelatedWord

// 1. STATE (Đổi VocabularyData -> VocabularyState)
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

// 2. EVENT
sealed interface VocabularyEvent {
    data object OnGoBack : VocabularyEvent
    data object OnNextWord : VocabularyEvent
    data class OnPlayAudio(val region: AudioRegion) : VocabularyEvent
    data class OnSelectOption(val index: Int) : VocabularyEvent
    data object OnNavigateToReviewScreen : VocabularyEvent
    data object OnRevealHint : VocabularyEvent
}

// 3. EFFECT
sealed interface VocabularyEffect {
    data object NavigateBack : VocabularyEffect
    data object NavigateToReview : VocabularyEffect
    data class PlayAudio(val region: AudioRegion) : VocabularyEffect
}