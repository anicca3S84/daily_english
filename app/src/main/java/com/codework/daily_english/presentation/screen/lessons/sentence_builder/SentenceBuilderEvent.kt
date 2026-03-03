package com.codework.daily_english.presentation.screen.lessons.sentence_builder

sealed interface SentenceBuilderEvent {
    data object OnGoBack : SentenceBuilderEvent
    data object OnToggleTheory : SentenceBuilderEvent // Bật/Tắt modal lý thuyết
    data object OnCheckAnswer : SentenceBuilderEvent
    data object OnNextQuestion : SentenceBuilderEvent
    
    // Logic game
    data class OnSelectWord(val wordChip: WordChip) : SentenceBuilderEvent // Từ pool -> answer
    data class OnDeselectWord(val wordChip: WordChip) : SentenceBuilderEvent // Từ answer -> pool
}