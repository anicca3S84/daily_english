package com.codework.dailyenglish.data.lesson.remote.dto

import com.google.gson.annotations.SerializedName

data class LessonResultResponseDto(
    @SerializedName("scorePercentage") val scorePercentage: Int?,
    @SerializedName("starsEarned") val starsEarned: Int?,
    @SerializedName("xpEarned") val xpEarned: Int?
)