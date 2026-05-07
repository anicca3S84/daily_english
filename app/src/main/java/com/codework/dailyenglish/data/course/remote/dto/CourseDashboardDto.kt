package com.codework.dailyenglish.data.course.remote.dto

import com.google.gson.annotations.SerializedName

data class CourseDashboardDto(
    @SerializedName("userStats")
    val userStats: UserStatsDto?,
    @SerializedName("courses")
    val courses: List<CourseDto>?
)