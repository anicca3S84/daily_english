package com.codework.dailyenglish.data.lesson.remote.dto

import com.google.gson.JsonElement
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
    @SerializedName("word") val word: String?,
    @SerializedName("meaning") val meaning: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("ukPhonetic") val ukPhonetic: String?,
    @SerializedName("usPhonetic") val usPhonetic: String?,
    @SerializedName("ukAudioUrl") val ukAudioUrl: String?,
    @SerializedName("usAudioUrl") val usAudioUrl: String?,
    @SerializedName("examples") val examples: List<String>?,
    @SerializedName("suggests") val suggests: List<String>?
) : StepDataDto

data class QuestionDto(
    @SerializedName("content") val content: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("selectedOptionId") val selectedOptionId: String?,
    @SerializedName("options") val options: List<OptionDto>?
) : StepDataDto

data class OptionDto(
    @SerializedName("id") val id: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("correct") val isCorrect: Boolean?
)

data class TheoryDto(
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?
) : StepDataDto