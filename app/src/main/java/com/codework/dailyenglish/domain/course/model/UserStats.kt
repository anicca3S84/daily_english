package com.codework.dailyenglish.domain.course.model

data class UserStats(
    val streak: Int,
    val totalExp: Int,
    val lessonsDone: Int,
    val wordsLearned: Int
)