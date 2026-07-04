package com.codework.dailyenglish.domain.lesson.model

sealed interface StepData {
    data class Vocab(
        val id: String,
        val type: String,
        val word: String,
        val meaning: String,
        val imageUrl: String?,
        val ukPhonetic: String?,
        val usPhonetic: String?,
        val ukAudioUrl: String?,
        val usAudioUrl: String?,
        val examples: List<String>?,
        val relatedWords: List<String>?,
        val isSaved: Boolean? = false
    ) : StepData

    data class Question(
        val id: String,
        val type: String,
        val content: QuestionContent,
        val userAnswer: String = "",
        val selectedIndices: List<Int> = emptyList(),
        val selectedOptionId: String? = null,
        val isAnswerEvaluated: Boolean = false,
        val isCorrect: Boolean? = null
    ) : StepData {
        val isUserAnswered: Boolean
            get() = userAnswer.isNotBlank() || selectedIndices.isNotEmpty() || !selectedOptionId.isNullOrEmpty()
    }

    data class Theory(
        val title: String?,
        val content: String
    ) : StepData

    data object Unknown : StepData
}

sealed class QuestionContent {
    data class MultipleChoice(
        val prompt: String,
        val options: List<Option>,
        val audioUrl: String?,
        val translation: String?
    ) : QuestionContent() {
        data class Option(val id: String, val text: String, val isCorrect: Boolean)
    }

    data class SentenceBuilder(
        val prompt: String,
        val correctSentence: String,
        val words: List<String>,
    ) : QuestionContent()

    data class Matching(val prompt: String, val pairs: List<PairData>) : QuestionContent() {
        data class PairData(val left: String, val right: String)
    }

    data class FillBlank(
        val prompt: String,
        val sentenceWithBlank: String,
        val correctAnswer: String,
        val options: List<String>,
        val hintMask: List<String>
    ) : QuestionContent()

    data class Dictation(
        val prompt: String,
        val targetWord: String,
        val hint: String,
        val correctAnswer: String,
        val acceptedAnswers: List<String>
    ) : QuestionContent()

    data class Speaking(val prompt: String, val targetText: String, val phonetic: String, val translation: String) :
        QuestionContent()

    object Unknown : QuestionContent()
}