package com.codework.dailyenglish.presentation.screen.lessons.sentence_builder

import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.presentation.screen.lessons.sentence_builder.model.SentenceBuilderQuestion
import com.codework.dailyenglish.presentation.screen.lessons.sentence_builder.model.WordChip
import com.codework.dailyenglish.presentation.screen.lessons.sentence_builder.mvi.SentenceBuilderEffect
import com.codework.dailyenglish.presentation.screen.lessons.sentence_builder.mvi.SentenceBuilderEvent
import com.codework.dailyenglish.presentation.screen.lessons.sentence_builder.mvi.SentenceBuilderState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SentenceBuilderViewModel @Inject constructor() :
    BaseViewModel<SentenceBuilderState, SentenceBuilderEvent, SentenceBuilderEffect>(
        initialState = SentenceBuilderState()
    ) {

    init {
        loadData()
    }

    override fun onEvent(event: SentenceBuilderEvent) {
//        val currentState = (_uiState.value as? BaseState.Success)?.data ?: return

        when (event) {
            SentenceBuilderEvent.OnGoBack -> sendEffect(SentenceBuilderEffect.NavigateBack)

            SentenceBuilderEvent.OnToggleTheory -> {
//                _uiState.update { BaseState.Success(currentState.copy(isTheoryVisible = !currentState.isTheoryVisible)) }
            }

            is SentenceBuilderEvent.OnSelectWord -> {
//                val newSelected = currentState.selectedWords + event.wordChip
//                val newAvailable = currentState.availableWords - event.wordChip
//                _uiState.update {
//                    BaseState.Success(currentState.copy(selectedWords = newSelected, availableWords = newAvailable, isCorrect = null))
//                }
            }

            is SentenceBuilderEvent.OnDeselectWord -> {
//                val newSelected = currentState.selectedWords - event.wordChip
//                val newAvailable = currentState.availableWords + event.wordChip
//                _uiState.update {
//                    BaseState.Success(currentState.copy(selectedWords = newSelected, availableWords = newAvailable, isCorrect = null))
//                }
            }

            SentenceBuilderEvent.OnCheckAnswer -> {
//                val currentQ = currentState.currentQuestion ?: return
//                val userSentence = currentState.selectedWords.map { it.text }
//                val isCorrect = userSentence == currentQ.correctSentence
//                _uiState.update { BaseState.Success(currentState.copy(isCorrect = isCorrect)) }
            }

            SentenceBuilderEvent.OnNextQuestion -> {
//                val nextIndex = currentState.currentIndex + 1
//                if (nextIndex < currentState.questions.size) {
//                    prepareQuestion(currentState.questions, nextIndex)
//                } else {
//                    sendEffect(SentenceBuilderEffect.NavigateToSpeaking)
//                }
            }

            SentenceBuilderEvent.OnRetry -> loadData()
        }
    }

    private fun loadData() {
//        viewModelScope.launch {
//            _state.update { BaseState.Loading }
//            delay(500)
//            val questions = listOf(
//                SentenceBuilderQuestion(
//                    id = "q1",
//                    vietnamese = "Tôi muốn một tách trà.",
//                    correctSentence = listOf("I", "'d", "like", "a", "cup", "of", "tea", "please"),
//                    grammarTitle = "Would like + Noun",
//                    grammarContent = "We use 'would like' or 'I'd like' to politely ask for something.",
//                    grammarExample = "I'd like a ticket to London, please."
//                ),
//                // ... Thêm các câu hỏi khác ...
//            )
//            prepareQuestion(questions, 0)
//        }
    }

    private fun prepareQuestion(questions: List<SentenceBuilderQuestion>, index: Int) {
        val currentQ = questions[index]
        val wordChips = currentQ.correctSentence.map { WordChip(text = it) }.shuffled()

//        _state.update {
//            BaseState.Success(
//                SentenceBuilderState(
//                    questions = questions,
//                    currentIndex = index,
//                    progressCurrent = index + 1,
//                    progressTotal = questions.size,
//                    currentQuestion = currentQ,
//                    selectedWords = emptyList(),
//                    availableWords = wordChips,
//                    isTheoryVisible = true,
//                    isCorrect = null
//                )
//            )
//        }
    }
}