package com.codework.dailyenglish.domain.course_detail.model

import com.codework.dailyenglish.presentation.screen.course_details.model.SkillType

data class LessonSkillDomain(
    val type: SkillType,
    val isDone: Boolean
)