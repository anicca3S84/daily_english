package com.codework.dailyenglish.domain.course_detail.model

import com.codework.dailyenglish.presentation.screen.course_details.model.LessonStatus

data class LessonDomain(
    val id: String,
    val title: String,
    val subTitle: String,
    val description: String,
    val status: LessonStatus,
    val progress: Float,
    val expReward: Int,
    val skills: List<LessonSkillDomain>
)