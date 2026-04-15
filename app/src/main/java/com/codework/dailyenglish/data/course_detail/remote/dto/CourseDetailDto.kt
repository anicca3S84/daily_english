package com.codework.dailyenglish.data.course_detail.remote.dto

data class CourseDetailDto(
    val courseTitle: String?,
    val overallProgress: Float?,
    val totalLessons: Int?,
    val completedLessons: Int?,
    val modules: List<ModuleDto>?
)