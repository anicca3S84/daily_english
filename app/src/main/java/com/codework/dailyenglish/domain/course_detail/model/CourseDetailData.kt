package com.codework.dailyenglish.domain.course_detail.model

data class CourseDetailData(
    val courseTitle: String,
    val cefr: String,
    val duration: String,
    val introduction: String,
    val overallProgress: Float,
    val totalLessons: Int,
    val completedLessons: Int,
    val modules: List<ModuleDomain>
)