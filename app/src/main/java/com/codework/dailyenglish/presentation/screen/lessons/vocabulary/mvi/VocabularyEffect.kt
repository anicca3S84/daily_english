package com.codework.dailyenglish.presentation.screen.lessons.vocabulary.mvi

import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.presentation.components.AudioRegion

sealed interface VocabularyEffect: BaseEffect {
    data object NavigateBack : VocabularyEffect
    data object NavigateToReview : VocabularyEffect
    data class PlayAudio(val region: AudioRegion) : VocabularyEffect
}