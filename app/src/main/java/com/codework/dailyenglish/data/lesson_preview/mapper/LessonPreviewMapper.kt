package com.codework.dailyenglish.data.lesson_preview.mapper
import com.codework.dailyenglish.data.lesson_preview.remote.dto.LessonPreviewDto
import com.codework.dailyenglish.domain.lesson_preview.model.LessonPreview

fun LessonPreviewDto.toDomain() = LessonPreview(
    id = id.orEmpty(),
    title = title.orEmpty(),
    duration = duration.orEmpty(),
    subtitle = subtitle.orEmpty(),
    content = content.orEmpty(),
    description = description.orEmpty(),
    vocabCount = vocabCount ?: 0,
    grammarCount = grammarCount ?: 0,
    xpReward = xpReward ?: 0,
    outcomes = outcomes ?: emptyList()
)