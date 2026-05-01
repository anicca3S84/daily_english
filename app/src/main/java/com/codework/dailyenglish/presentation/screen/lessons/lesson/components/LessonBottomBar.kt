package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.domain.lesson.model.LessonStep
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.ButtonHeightLarge
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@Composable
fun LessonBottomBar(
    currentStep: LessonStep?,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEnabled = when (val data = currentStep?.data) {
        is StepData.Question -> data.selectedOptionId != null
        else -> true
    }

    val topColor = if (isEnabled) Color(0xFF3B82F6) else Color(0xFFE5E7EB)
    val bottomColor = if (isEnabled) Color(0xFF2563EB) else Color(0xFF9CA3AF)
    val contentColor = if (isEnabled) Color.White else Color(0xFF9CA3AF)

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent // Hoặc Color.White tùy design của bạn
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center // <--- CHỐT HẠ: Đưa cái nút ra chính giữa
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth() // <--- Nút sẽ co dãn theo nội dung bên trong
                    .height(Spaces.Space64)
                    .background(bottomColor, RoundedCornerShape(Spaces.Space16))
                    .clip(RoundedCornerShape(Spaces.Space16))
                    .then(if (isEnabled) Modifier.clickable { onNextClick() } else Modifier)
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentWidth() // <--- Đồng bộ độ rộng
                        .fillMaxHeight()
                        .padding(bottom = if (isEnabled) Spaces.Space6 else 0.dp)
                        .background(topColor, RoundedCornerShape(Spaces.Space16)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spaces.Space32), // <--- Tạo khoảng trống 2 bên chữ cho đẹp
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(AppIcons.IconSizeMedium)
                        )
                        Spacer(modifier = Modifier.width(Spaces.Space8))
                        Text(
                            text = stringResource(R.string.label_continue).uppercase(),
                            color = contentColor,
                            fontWeight = FontWeight.Black,
                            fontSize = AppTextSizes.TextSizeTitle,
                            letterSpacing = Spaces.Space1.value.sp,
                            maxLines = 1 // Đảm bảo chữ không bị xuống dòng
                        )
                    }
                }
            }
        }
    }
}