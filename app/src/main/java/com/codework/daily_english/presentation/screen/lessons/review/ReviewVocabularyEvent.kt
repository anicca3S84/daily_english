package com.codework.daily_english.presentation.screen.lessons.review


sealed interface ReviewVocabularyEvent {
    data class OnSelectOption(val index: Int) : ReviewVocabularyEvent
    data object OnNextQuestion : ReviewVocabularyEvent
    data object OnGoBack : ReviewVocabularyEvent
    data object OnNavigateToGrammarScreen : ReviewVocabularyEvent

}