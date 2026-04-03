package com.codework.daily_english.data.course_detail.remote.dto

data class ModuleDto(
    val title: String?,
    val description: String?,
    val lessons: List<LessonDto>?
)