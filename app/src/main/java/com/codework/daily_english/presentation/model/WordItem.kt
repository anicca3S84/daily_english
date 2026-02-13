package com.codework.daily_english.presentation.model

import java.util.UUID

data class WordItem(
    val word: String,
    val type: String,
    val phoneticUk: String,
    val phoneticUs: String,
    val definition: String,
    val example: String,
    val isSaved: Boolean = false,
    val synonyms: List<String> = emptyList(), // Thêm cái này
    val antonyms: List<String> = emptyList()  // Thêm cái này
)