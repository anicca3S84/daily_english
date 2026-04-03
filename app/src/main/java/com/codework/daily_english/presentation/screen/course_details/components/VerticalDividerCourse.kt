package com.codework.daily_english.presentation.screen.course_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.codework.daily_english.presentation.theme.Spaces

@Composable
fun VerticalDividerCourse() {
    Box(modifier = Modifier.width(Spaces.Space1)
        .height(Spaces.Space30)
        .background(Color.LightGray.copy(alpha = 0.5f)))
}