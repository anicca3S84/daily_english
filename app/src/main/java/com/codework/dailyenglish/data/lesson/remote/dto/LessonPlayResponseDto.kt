package com.codework.dailyenglish.data.lesson.remote.dto

import com.google.gson.annotations.SerializedName

data class LessonPlayResponseDto(
    @SerializedName("lessonId") val lessonId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("resumeCursor") val resumeCursor: Int?,
    @SerializedName("steps") val steps: List<LessonStepDto>?
)

data class LessonStepDto(
    @SerializedName("stepId") val stepId: Long?,
    @SerializedName("refId") val refId: String?,
    @SerializedName("stepType") val stepType: String?,
    @SerializedName("orderIndex") val orderIndex: Int?,
    @SerializedName("data") val data: StepDataDto?
)

interface StepDataDto

data class VocabDto(
    @SerializedName("id") val id: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("word") val word: String?,
    @SerializedName("meaning") val meaning: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("ukPhonetic") val ukPhonetic: String?,
    @SerializedName("usPhonetic") val usPhonetic: String?,
    @SerializedName("ukAudioUrl") val ukAudioUrl: String?,
    @SerializedName("usAudioUrl") val usAudioUrl: String?,
    @SerializedName("examples") val examples: List<String>?,
    @SerializedName("relatedWords") val relatedWords: List<String>?,
    @SerializedName("isSaved") val isSaved: Boolean?
) : StepDataDto

data class QuestionDto(
    val id: String?,
    val type: String?,
    val content: QuestionContentDto?,
    @SerializedName("selectedOptionId") val selectedOptionId: String?,
    @SerializedName("userAnswer") val userAnswer: String?,
    @SerializedName("selectedIndices") val selectedIndices: List<Int>?,
    @SerializedName("isCorrect") val isCorrect: Boolean?
) : StepDataDto


data class TheoryDto(
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?
) : StepDataDto


sealed class QuestionContentDto {
    data class MultipleChoice(
        val prompt: String?,
        val audioUrl: String?,
        val translation: String?,
        val options: List<OptionDto>?
    ) : QuestionContentDto() {
        data class OptionDto(
            val id: String?,
            val text: String?,
            @SerializedName("isCorrect") val isCorrect: Boolean?
        )
    }

    data class SentenceBuilder(
        @SerializedName("prompt") val prompt: String?,
        @SerializedName("correctSentence") val correctSentence: String?,
        @SerializedName("words") val words: List<String>?
    ) : QuestionContentDto()


    data class Matching(
        val prompt: String?,
        val pairs: List<PairDto>?
    ) : QuestionContentDto() {
        data class PairDto(
            val left: String?,
            val right: String?
        )
    }

    data class FillBlank(
        val prompt: String?,
        val sentenceWithBlank: String?,
        val correctAnswer: String?,
        val options: List<String>?,
        val hintMask: List<String>?
    ) : QuestionContentDto()

    data class Dictation(
        val prompt: String?,
        val targetWord: String?,
        val hint: String?,
        val correctAnswer: String?,
        val acceptedAnswers: List<String>?
    ) : QuestionContentDto()

    data class Speaking(
        val prompt: String?,
        val targetText: String?,
        val phonetic: String?,
        val translation: String?
    ) : QuestionContentDto()
}