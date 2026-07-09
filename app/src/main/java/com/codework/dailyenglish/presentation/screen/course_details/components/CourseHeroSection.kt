package com.codework.dailyenglish.presentation.screen.course_details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.R // Đừng quên import R của project
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailState
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun CourseHeroSection(state: CourseDetailState) {

    val backgroundImageRes = when {
        state.courseTitle.contains("Rookie", ignoreCase = true) -> R.drawable.img_course_a1
        state.courseTitle.contains("Explorer", ignoreCase = true) -> R.drawable.img_course_a2
        state.courseTitle.contains("Communicator", ignoreCase = true) -> R.drawable.img_course_b1
        state.courseTitle.contains("Fluent", ignoreCase = true) -> R.drawable.img_course_b2
        state.courseTitle.contains("Master", ignoreCase = true) -> R.drawable.img_course_c1
        state.courseTitle.contains("Legendary", ignoreCase = true) -> R.drawable.img_course_c2
        else -> R.drawable.img_course_a1
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spaces.Space200 + Spaces.Space20)
            .clip(RoundedCornerShape(AppRadius.RadiusExtraLarge))
            .background(state.courseTheme.primaryColor)
    ) {
        Image(
            painter = painterResource(id = backgroundImageRes),
            contentDescription = state.courseTitle,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}