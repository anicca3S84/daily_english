package com.codework.dailyenglish.domain.vocabulary.model


data class VocabTopic(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val totalWords: Int,
)