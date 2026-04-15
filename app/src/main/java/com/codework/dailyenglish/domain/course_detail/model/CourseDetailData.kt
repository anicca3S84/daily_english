package com.codework.dailyenglish.domain.course_detail.model

data class CourseDetailData(
    val courseTitle: String,
    val overallProgress: Float,
    val totalLessons: Int,
    val completedLessons: Int,
    val modules: List<ModuleDomain>
)