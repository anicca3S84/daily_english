package com.codework.daily_english.presentation.screen.lessons.vocabulary.model

data class RelatedWord(
    val word: String,           // VD: place to stay
    val definition: String? = null,
    val example: String         // VD: If you need a place to stay...
)