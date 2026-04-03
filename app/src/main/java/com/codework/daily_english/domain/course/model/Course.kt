package com.codework.daily_english.domain.course.model

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val cefr: String,
    val lessons: Int,
    val progress: Float,
    val isLocked: Boolean,
    val code: String
)