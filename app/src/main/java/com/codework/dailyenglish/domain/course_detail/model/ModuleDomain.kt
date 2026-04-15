package com.codework.dailyenglish.domain.course_detail.model

data class ModuleDomain(
    val title: String,
    val description: String,
    val lessons: List<LessonDomain>
)