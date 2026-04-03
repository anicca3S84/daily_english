package com.codework.daily_english.data.course_detail.mapper

import com.codework.daily_english.core.utils.Constants
import com.codework.daily_english.data.course_detail.remote.dto.*
import com.codework.daily_english.domain.course_detail.model.*
import com.codework.daily_english.presentation.screen.course_details.model.LessonStatus
import com.codework.daily_english.presentation.screen.course_details.model.SkillType

fun CourseDetailDto.toDomain(): CourseDetailData {
    return CourseDetailData(
        courseTitle = this.courseTitle.orEmpty(),
        overallProgress = this.overallProgress ?: 0f,
        totalLessons = this.totalLessons ?: 0,
        completedLessons = this.completedLessons ?: 0,
        modules = this.modules?.map { it.toDomain() } ?: emptyList()
    )
}

fun ModuleDto.toDomain(): ModuleDomain {
    return ModuleDomain(
        title = this.title.orEmpty(),
        description = this.description.orEmpty(),
        lessons = this.lessons?.map { it.toDomain() } ?: emptyList()
    )
}

fun LessonDto.toDomain(): LessonDomain {
    val statusEnum = when (this.status?.uppercase()) {
        Constants.COMPLETED -> LessonStatus.COMPLETED
        Constants.LOCKED -> LessonStatus.LOCKED
        else -> LessonStatus.IN_PROGRESS
    }

    return LessonDomain(
        id = this.id ?: Constants.NO_ID,
        title = this.title.orEmpty(),
        description = this.description.orEmpty(),
        status = statusEnum,
        progress = this.progress ?: 0f,
        expReward = this.expReward ?: 0,
        skills = this.skills?.map { it.toDomain() } ?: emptyList()
    )
}

fun LessonSkillDto.toDomain(): LessonSkillDomain {
    val skillTypeEnum = when (this.type?.uppercase()) {
        Constants.LISTEN -> SkillType.LISTEN
        Constants.GRAM -> SkillType.GRAM
        Constants.SPEAK -> SkillType.SPEAK
        else -> SkillType.VOCAB
    }

    return LessonSkillDomain(
        type = skillTypeEnum,
        isDone = this.isDone ?: false
    )
}