package com.codework.dailyenglish.presentation.screen.course_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@Composable
fun CourseHeroSection(state: CourseDetailState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spaces.Space200 + Spaces.Space20)
            .clip(RoundedCornerShape(AppRadius.RadiusExtraLarge))
            .background(state.courseTheme.primaryColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 100f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spaces.Space20),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = state.courseTitle,
                color = Color.White,
                fontSize = AppTextSizes.TextSizeHeader,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 30.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}