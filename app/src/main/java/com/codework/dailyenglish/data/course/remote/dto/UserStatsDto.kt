package com.codework.dailyenglish.data.course.remote.dto

import com.google.gson.annotations.SerializedName

data class UserStatsDto(
    @SerializedName("streak")
    val streak: Int?,
    @SerializedName("totalExp")
    val totalExp: Int?,
    @SerializedName("lessonsDone")
    val lessonsDone: Int?,
    @SerializedName("wordsLearned")
    val wordsLearned: Int?
)