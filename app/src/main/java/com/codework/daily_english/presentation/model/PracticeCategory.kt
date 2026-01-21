package com.codework.daily_english.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class PracticeCategory(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)