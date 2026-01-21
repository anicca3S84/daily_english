package com.codework.daily_english.presentation.model

data class LessonStep(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: LessonStepType,
    val status: StepStatus
)