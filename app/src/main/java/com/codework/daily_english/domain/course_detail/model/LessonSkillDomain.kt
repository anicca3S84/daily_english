package com.codework.daily_english.domain.course_detail.model

import com.codework.daily_english.presentation.screen.course_details.model.SkillType

data class LessonSkillDomain(
    val type: SkillType,
    val isDone: Boolean
)