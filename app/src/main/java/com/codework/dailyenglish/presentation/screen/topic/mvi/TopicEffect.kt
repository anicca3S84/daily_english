package com.codework.dailyenglish.presentation.screen.topic.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface TopicEffect : BaseEffect {
    data class NavigateToVocabList(val topicId: String, val topicName: String) : TopicEffect

    data class NavigateToAiPractice(val topicId: String, val topicName: String) : TopicEffect
}