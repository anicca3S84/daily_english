package com.codework.dailyenglish.domain.course.model

data class CourseDashboardData(
    val stats: UserStats,
    val courses: List<Course>
)