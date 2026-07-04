package com.codework.dailyenglish.presentation.screen.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.navigation.Route
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.vocabulary.usecase.GetVocabTopicsUseCase
import com.codework.dailyenglish.presentation.screen.topic.mvi.*
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val getVocabTopicsUseCase: GetVocabTopicsUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<TopicState, TopicEvent, TopicEffect>(TopicState()) {
    val origin = savedStateHandle.toRoute<Route.TopicScreen>().origin
    init {
        onEvent(TopicEvent.LoadTopics)
    }

    override fun onEvent(event: TopicEvent) {
        when (event) {
            TopicEvent.LoadTopics -> fetchTopics()
            is TopicEvent.OnTopicClick -> {
                if (origin == "DICTIONARY") {
                    sendEffect(TopicEffect.NavigateToVocabList(event.topic.id, event.topic.name))
                } else {
                    sendEffect(TopicEffect.NavigateToAiPractice(event.topic.id, event.topic.name))
                }
            }
        }
    }


    private fun fetchTopics() {
        _state.update { it.copy(topics = it.topics.copy(isLoading = true)) }
        viewModelScope.launch {
            getVocabTopicsUseCase()
                .onSuccess { list ->
                    _state.update { it.copy(topics = it.topics.copy(data = list, isLoading = false)) }
                }
                .onError { err ->
                    _state.update { it.copy(topics = it.topics.copy(isLoading = false, error = err.asUiText())) }
                }
        }
    }
}