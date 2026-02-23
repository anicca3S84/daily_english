package com.codework.daily_english.presentation.screen.lessons.vocabulary

import com.codework.daily_english.presentation.components.AudioRegion

sealed interface VocabularyEvent {
    data object OnGoBack : VocabularyEvent
    data object OnNextWord : VocabularyEvent

    // SỬA: Chỉ cần truyền Region (UK/US), bỏ Speed
    data class OnPlayAudio(val region: AudioRegion) : VocabularyEvent

    data class OnSelectOption(val index: Int) : VocabularyEvent
    data object OnNavigateToReviewScreen : VocabularyEvent
    data object OnRevealHint : VocabularyEvent
}