package com.codework.dailyenglish.data.lesson_preview.remote.dto

import com.google.gson.annotations.SerializedName

data class LessonPreviewDto(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("subTitle") val subtitle: String?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("outcomes") val outcomes: List<String>?,
    @SerializedName("description") val description: String?,
    @SerializedName("vocabCount") val vocabCount: Int?,
    @SerializedName("grammarCount") val grammarCount: Int?,
    @SerializedName("xpReward") val xpReward: Int?
)