package com.codework.dailyenglish.data.lesson.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProgressRequestDto(
    @SerializedName("resumeCursor") val resumeCursor: Int,
    @SerializedName("questionId") val questionId: String? = null,
    @SerializedName("selectedOptionId") val selectedOptionId: String? = null,
    @SerializedName("isCorrect") val isCorrect: Boolean? = null
)