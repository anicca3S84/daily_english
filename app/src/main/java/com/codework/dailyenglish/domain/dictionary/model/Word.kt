package com.codework.dailyenglish.domain.dictionary.model

import com.google.gson.annotations.SerializedName

data class Word(
    val id: String,
    val type: String,
    val word: String,
    val meaning: String,
    val imageUrl: String,
    val ukPhonetic: String,
    val usPhonetic: String,
    val ukAudioUrl: String,
    val usAudioUrl: String,
    val examples: List<String>,
    val relatedWords: List<String>,
    val isSaved: Boolean
)