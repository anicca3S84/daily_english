package com.codework.daily_english.data.course.mapper

import com.codework.daily_english.data.course.remote.dto.CourseDashboardDto
import com.codework.daily_english.data.course.remote.dto.CourseDto
import com.codework.daily_english.data.course.remote.dto.UserStatsDto
import com.codework.daily_english.domain.course.model.Course
import com.codework.daily_english.domain.course.model.CourseDashboardData
import com.codework.daily_english.domain.course.model.UserStats

fun UserStatsDto.toDomain() = UserStats(
    streak = streak ?: 0,
    totalExp = totalExp ?: 0,
    lessonsDone = lessonsDone ?: 0,
    wordsLearned = wordsLearned ?: 0
)

fun CourseDto.toDomain() = Course(
    id = id ?: "no_id_${System.currentTimeMillis()}",
    title = title.orEmpty(),
    description = description.orEmpty(),
    cefr = cefr.orEmpty(),
    lessons = lessons ?: 0,
    progress = progress ?: 0f,
    isLocked = isLocked ?: true,
    code = code ?: "default"
)

fun CourseDashboardDto.toDomain() = CourseDashboardData(
    stats = userStats?.toDomain() ?: UserStats(0, 0, 0, 0),
    courses = courses?.map { it.toDomain() } ?: emptyList()
)