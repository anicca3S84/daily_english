package com.codework.dailyenglish.domain.lesson.model

sealed interface StepData {
    data class Vocab(
        val word: String,
        val meaning: String,
        val imageUrl: String?,
        val ukPhonetic: String?,
        val usPhonetic: String?,
        val ukAudioUrl: String?,
        val usAudioUrl: String?,
        val examples: List<String>?,
        val suggests: List<String>?
    ) : StepData

    data class Question(
        val content: String,
        val type: String,
        val options: List<Option>,
        val selectedOptionId: String? = null,
        val isChecked: Boolean = false
    ) : StepData {
        data class Option(
            val id: String,
            val content: String,
            val isCorrect: Boolean
        )
    }

    data class Theory(
        val title: String?,
        val content: String
    ) : StepData

    data object Unknown : StepData
}