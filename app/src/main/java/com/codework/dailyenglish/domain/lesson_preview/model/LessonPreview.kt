package com.codework.dailyenglish.domain.lesson_preview.model

data class LessonPreview(
    val id: String,
    val title: String,
    val duration: String,
    val subtitle: String,
    val content: String,
    val description: String,
    val vocabCount: Int,
    val grammarCount: Int,
    val xpReward: Int,
    val outcomes: List<String>
)