package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface // Hoặc androidx.compose.material.Surface nếu bạn dùng M2
import androidx.compose.material3.Text    // Hoặc androidx.compose.material.Text nếu bạn dùng M2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun FlatQuizOption(
    text: String,
    letter: String,
    optionId: String,
    selectedOptionId: String?,
    isCorrect: Boolean,
    isEvaluated: Boolean,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
     val ColorTextMain = Color(0xFF1E293B)
     val ColorGrayBorder = Color(0xFFE2E8F0)

     val ColorBlueBg = Color(0xFFEFF3FF)

// Màu trạng thái đánh giá (Nếu có dùng cho kiểm tra đáp án)
     val ColorGreenMain = Color(0xFF10B981)
    val ColorGreenBg = Color(0xFFD1FAE5)

     val ColorRedMain = Color(0xFFEF4444)
     val ColorRedBg = Color(0xFFFEE2E2)
    val isSelected = selectedOptionId == optionId

    // Chuyển đổi màu sắc khi được chọn hoặc kiểm tra đúng/sai
    val bgColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> ColorGreenBg
            isEvaluated && isSelected && !isCorrect -> ColorRedBg
            !isEvaluated && isSelected -> ColorBlueBg
            else -> Color.White
        }, label = "BgColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> ColorGreenMain
            isEvaluated && isSelected && !isCorrect -> ColorRedMain
            !isEvaluated && isSelected -> AppColors.BluePrimary
            else -> ColorGrayBorder
        }, label = "BorderColor"
    )

    val badgeBgColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> ColorGreenMain
            isEvaluated && isSelected && !isCorrect -> ColorRedMain
            !isEvaluated && isSelected -> AppColors.BluePrimary
            else -> ColorBlueBg
        }, label = "BadgeBgColor"
    )

    val badgeTextColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> Color.White
            isEvaluated && isSelected && !isCorrect -> Color.White
            !isEvaluated && isSelected -> Color.White
            else -> AppColors.BluePrimary
        }, label = "BadgeTextColor"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> ColorGreenMain
            isEvaluated && isSelected && !isCorrect -> ColorRedMain
            !isEvaluated && isSelected -> AppColors.BluePrimary
            else -> ColorTextMain
        }, label = "TextColor"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = !isEvaluated) { onClick(optionId) },
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.5.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Vòng tròn chữ cái chỉ mục (A, B, C, D)
            Surface(
                shape = CircleShape,
                color = badgeBgColor,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = letter,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor
                    )
                }
            }

            // Nội dung đáp án dạng text
            Text(
                text = text,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}