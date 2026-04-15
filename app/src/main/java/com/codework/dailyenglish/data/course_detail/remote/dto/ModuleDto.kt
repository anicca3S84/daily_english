package com.codework.dailyenglish.data.course_detail.remote.dto

data class ModuleDto(
    val title: String?,
    val description: String?,
    val lessons: List<LessonDto>?
)