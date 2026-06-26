package com.codework.dailyenglish.data.user_statistic.remote.dto


import com.google.gson.annotations.SerializedName

data class WeeklyStudyStatsDto(
    @SerializedName("date") val date: String, // YYYY-MM-DD
    @SerializedName("secondsStudied") val secondsStudied: Int,
    @SerializedName("today") val isToday: Boolean
)