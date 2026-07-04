package com.codework.dailyenglish.domain.dictionary.model

data class Word(
    val id: String,
    val type: String,
    var word: String,
    val meaning: String,
    val imageUrl: String,
    val ukPhonetic: String,
    val usPhonetic: String,
    val ukAudioUrl: String,
    val usAudioUrl: String,
    val examples: List<String>,
    val relatedWords: List<String>,
    val isSaved: Boolean,
    val definitions: List<DefinitionDetail>
)