package com.codework.daily_english.data.course_detail.remote.dto

data class LessonDto(
    val id: String?,
    val title: String?,
    val description: String?,
    val status: String?,
    val progress: Float?,
    val expReward: Int?,
    val skills: List<LessonSkillDto>?
)