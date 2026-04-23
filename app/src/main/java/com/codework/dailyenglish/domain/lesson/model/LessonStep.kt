package com.codework.dailyenglish.domain.lesson.model

data class LessonStep(
    val stepId: Long,
    val refId: String,
    val stepType: StepType,
    val orderIndex: Int,
    val data: StepData
)