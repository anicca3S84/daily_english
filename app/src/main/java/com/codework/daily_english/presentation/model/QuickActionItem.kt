package com.codework.daily_english.presentation.model
import androidx.compose.ui.graphics.Color

data class QuickActionItem(
    val icon: Int,
    val label: String,
    val color: Color,
    val onClick: () -> Unit,
)