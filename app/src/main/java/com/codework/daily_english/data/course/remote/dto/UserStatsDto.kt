package com.codework.daily_english.data.course.remote.dto

import com.google.gson.annotations.SerializedName

data class UserStatsDto(
    @SerializedName("streak")
    val streak: Int?,
    @SerializedName("total_exp")
    val totalExp: Int?,
    @SerializedName("lessons_done")
    val lessonsDone: Int?,
    @SerializedName("words_learned")
    val wordsLearned: Int?
)