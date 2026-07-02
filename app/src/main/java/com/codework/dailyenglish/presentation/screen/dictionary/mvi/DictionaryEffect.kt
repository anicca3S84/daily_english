package com.codework.dailyenglish.presentation.screen.dictionary.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface DictionaryEffect : BaseEffect {
    data class PlayAudio(val word: String, val accent: String) : DictionaryEffect
    data class NavigateToTopicWords(
        val topicPublicId: String,
        val topicName: String
    ) : DictionaryEffect
    data class NavigateToWordDetail(
        val wordId: String,
    ) : DictionaryEffect
    data class NavigateToTopicSelection(val origin: String) : DictionaryEffect
}


