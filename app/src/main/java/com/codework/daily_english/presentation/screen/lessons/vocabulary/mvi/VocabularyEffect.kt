package com.codework.daily_english.presentation.screen.lessons.vocabulary.mvi

import com.codework.daily_english.core.base.BaseEffect
import com.codework.daily_english.presentation.components.AudioRegion
import com.codework.daily_english.presentation.screen.lessons.vocabulary.model.RelatedWord

sealed interface VocabularyEffect: BaseEffect {
    data object NavigateBack : VocabularyEffect
    data object NavigateToReview : VocabularyEffect
    data class PlayAudio(val region: AudioRegion) : VocabularyEffect
}