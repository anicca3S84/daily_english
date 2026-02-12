package com.codework.daily_english.presentation.model

import java.util.UUID

data class WordItem(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val phoneticUk: String,
    val phoneticUs: String,
    val type: String,
    val definition: String,
    val example: String,
    val isSaved: Boolean = false
)