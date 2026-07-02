package com.codework.dailyenglish.presentation.screen.topic.mvi

import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic

sealed interface TopicEvent {
    object LoadTopics : TopicEvent
    data class OnTopicClick(val topic: VocabTopic) : TopicEvent
}