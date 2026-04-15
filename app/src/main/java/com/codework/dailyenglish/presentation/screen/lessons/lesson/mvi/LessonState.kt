package com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi

import com.codework.dailyenglish.presentation.screen.lessons.lesson.model.LessonStep

data class LessonState(
    val lessonId: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val totalProgress: Float = 0.0f,
    val steps: List<LessonStep> = emptyList(),
    val level: String = ""
)