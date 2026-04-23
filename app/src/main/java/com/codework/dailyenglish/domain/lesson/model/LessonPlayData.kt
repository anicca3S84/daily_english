package com.codework.dailyenglish.domain.lesson.model

data class LessonPlay(
    val lessonId: String,
    val title: String,
    val resumeCursor: Int,
    val steps: List<LessonStep>
)