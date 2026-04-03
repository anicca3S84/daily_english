package com.codework.daily_english.data.course.remote.dto

import com.google.gson.annotations.SerializedName

data class CourseDashboardDto(
    @SerializedName("user_stats")
    val userStats: UserStatsDto?,
    @SerializedName("courses")
    val courses: List<CourseDto>?
)