package com.codework.dailyenglish.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun AppCongratulationsScreen(
    scorePercentage: Int,
    starsEarned: Int,
    feedbackMessage: String = "Great job, keep going \uD83E\uDD73",
    primaryButtonText: String = "Continue",
    secondaryButtonText: String = "Review",
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(Spaces.Space24),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Phần thân chứa hình ảnh và nội dung (chiếm không gian giữa)
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                ConfettiBackground(modifier = Modifier.fillMaxSize())

                // 3 Ngôi sao
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(top = Spaces.Space32)
                ) {
                    // Sao 1 (Trái)
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star 1",
                        tint = if (starsEarned >= 1) Color(0xFFF59E0B) else AppColors.Slate200,
                        modifier = Modifier
                            .size(64.dp)
                            .offset(x = 16.dp, y = (-16).dp)
                    )
                    // Sao 2 (Giữa - To nhất)
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star 2",
                        tint = if (starsEarned >= 2) Color(0xFFF59E0B) else AppColors.Slate200,
                        modifier = Modifier
                            .size(100.dp)
                            .offset(y = (-32).dp)
                            .zIndex(1f)
                    )
                    // Sao 3 (Phải)
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star 3",
                        tint = if (starsEarned >= 3) Color(0xFFF59E0B) else AppColors.Slate200,
                        modifier = Modifier
                            .size(64.dp)
                            .offset(x = (-16).dp, y = (-16).dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spaces.Space32))

            // Tiêu đề
            Text(
                text = "Congratulations",
                fontSize = AppTextSizes.TextSize24, // Hoặc 28sp tùy theme
                fontWeight = FontWeight.Bold,
                color = AppColors.Slate800
            )

            Spacer(modifier = Modifier.height(Spaces.Space24))

            // Row hiển thị % và số sao
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spaces.Space32),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Phần trăm
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Score",
                        tint = Color(0xFF22C55E), // Green 500
                        modifier = Modifier.size(Spaces.Space24)
                    )
                    Spacer(modifier = Modifier.width(Spaces.Space8))
                    Text(
                        text = "$scorePercentage%",
                        fontSize = AppTextSizes.TextSize20,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Slate800
                    )
                }

                // Số sao đạt được
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Stars",
                        tint = Color(0xFFF59E0B), // Amber 500
                        modifier = Modifier.size(Spaces.Space28)
                    )
                    Spacer(modifier = Modifier.width(Spaces.Space8))
                    Text(
                        text = "$starsEarned",
                        fontSize = AppTextSizes.TextSize20,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Slate800
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spaces.Space32))

            // Chip Feedback ("Great job...")
            Surface(
                shape = RoundedCornerShape(AppRadius.RadiusFull),
                color = AppColors.Slate50, // Hoặc Color(0xFFF8FAFC)
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = feedbackMessage,
                    fontSize = AppTextSizes.TextSizeMain,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.Slate700,
                    modifier = Modifier.padding(
                        horizontal = Spaces.Space24,
                        vertical = Spaces.Space12
                    )
                )
            }
        }

        // Phần 2 nút bấm ở cuối màn hình
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spaces.Space16)
        ) {
            Button(
                onClick = onSecondaryClick,
                modifier = Modifier
                    .weight(1f)
                    .height(Spaces.Space52),
                shape = RoundedCornerShape(AppRadius.RadiusLarge),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Slate100, // Light Gray
                    contentColor = AppColors.Slate800
                )
            ) {
                Text(
                    text = secondaryButtonText,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppTextSizes.TextSizeMain
                )
            }
            // Nút Primary (Continue)
            Button(
                onClick = onPrimaryClick,
                modifier = Modifier
                    .weight(1f)
                    .height(Spaces.Space52),
                shape = RoundedCornerShape(AppRadius.RadiusLarge),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.BluePrimary // Blue Color từ mẫu
                )
            ) {
                Text(
                    text = primaryButtonText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppTextSizes.TextSizeMain
                )
            }

            // Nút Secondary (Review)

        }
    }
}

// Custom vẽ Pháo giấy bay lơ lửng không cần ảnh
@Composable
private fun ConfettiBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val colors = listOf(
            Color(0xFFFCD34D), // Vàng
            Color(0xFFFCA5A5), // Hồng
            Color(0xFF86EFAC), // Xanh lá
            Color(0xFF93C5FD), // Xanh dương
            Color(0xFFF97316)  // Cam
        )

        val drawConfetti = { xRatio: Float, yRatio: Float, rotation: Float, colorIdx: Int, isCircle: Boolean ->
            withTransform({
                translate(left = size.width * xRatio, top = size.height * yRatio)
                rotate(rotation)
            }) {
                if (isCircle) {
                    drawRoundRect(
                        color = colors[colorIdx],
                        size = Size(24f, 24f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                } else {
                    drawRect(
                        color = colors[colorIdx],
                        size = Size(16f, 40f)
                    )
                }
            }
        }

        // Vẽ vài mảnh pháo giấy theo tọa độ giả lập giống ảnh mẫu
        drawConfetti(0.2f, 0.1f, 30f, 0, false) // Vàng trái trên
        drawConfetti(0.1f, 0.3f, -45f, 1, false) // Hồng trái
        drawConfetti(0.25f, 0.5f, 20f, 2, false) // Xanh lá trái dưới
        drawConfetti(0.15f, 0.65f, -30f, 4, false) // Cam trái dưới

        drawConfetti(0.55f, 0.15f, -20f, 2, false) // Xanh giữa trên
        drawConfetti(0.5f, 0.4f, 15f, 1, false) // Hồng giữa

        drawConfetti(0.8f, 0.1f, 45f, 0, false) // Vàng phải trên
        drawConfetti(0.9f, 0.4f, -45f, 3, false) // Xanh dương phải
        drawConfetti(0.75f, 0.45f, 30f, 4, false) // Cam phải
        drawConfetti(0.85f, 0.65f, -20f, 1, false) // Hồng phải dưới
    }
}

@Preview(showBackground = true)
@Composable
fun AppCongratulationsScreenPreview() {
    AppCongratulationsScreen(
        scorePercentage = 100,
        starsEarned = 3,
        onPrimaryClick = {},
        onSecondaryClick = {}
    )
}