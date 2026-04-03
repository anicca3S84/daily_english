package com.codework.daily_english.presentation.screen.courses.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.codework.daily_english.presentation.theme.CourseTheme

data class CourseUi(
    val id: String,
    val code: String,
    val title: String,
    val description: String,
    val cefr: String,
    val lessons: Int,
    val progress: Float,
    val isLocked: Boolean,
    val themeColor: CourseTheme,
    val icon: ImageVector
)