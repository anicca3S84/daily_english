package com.codework.daily_english.domain.course.model

data class CourseDashboardData(
    val stats: UserStats,
    val courses: List<Course>
)