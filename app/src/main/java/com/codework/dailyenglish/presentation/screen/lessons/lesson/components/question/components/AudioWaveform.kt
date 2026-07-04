package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioWaveform(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF2E66F6) // Màu xanh dương chuẩn như trong ảnh mẫu
) {
    // Biên độ sóng âm được bóc tách tỉ mỉ từ hình ảnh mẫu của bạn (4 cụm âm và phần đuôi)
    val sourceAmplitudes = remember {
        listOf(
            0.12f, 0.18f, 0.28f, 0.45f, 0.72f, 0.88f, 0.62f, 0.40f, 0.25f, 0.18f, // Cụm 1: Lên cao rồi hạ xuống
            0.22f, 0.35f, 0.52f, 0.68f, 0.48f, 0.32f, 0.20f, 0.16f,             // Cụm 2: Nhấp nhô vừa phải
            0.25f, 0.42f, 0.65f, 0.82f, 0.90f, 0.70f, 0.45f, 0.28f, 0.22f,       // Cụm 3: Cao hơn cụm 2
            0.28f, 0.52f, 0.80f, 1.00f, 0.78f, 0.58f, 0.38f, 0.25f, 0.18f,       // Cụm 4: Đỉnh cao nhất (1.0)
            0.15f, 0.12f, 0.10f, 0.08f, 0.06f, 0.04f                             // Phần đuôi nhỏ dần về cuối
        )
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Tinh chỉnh độ mảnh và khoảng cách cột theo đúng tỉ lệ trong ảnh mẫu
        val barWidth = 2.dp.toPx() // Độ rộng cột mảnh hơn (2.dp)
        val gap = 2.dp.toPx()      // Khoảng cách giữa các cột khít hơn (2.dp)
        val totalBarWidth = barWidth + gap
        val totalBars = (width / totalBarWidth).toInt()

        for (i in 0 until totalBars) {
            // Tính toán vị trí tương đối (fraction) từ 0.0f đến 1.0f dọc theo chiều rộng Canvas
            val fraction = if (totalBars > 1) i.toFloat() / (totalBars - 1) else 0f

            // Sử dụng thuật toán nội suy (Lerp) để ánh xạ mảng biên độ gốc vào số lượng cột thực tế trên màn hình
            val sourcePosition = fraction * (sourceAmplitudes.size - 1)
            val lowerIndex = sourcePosition.toInt()
            val upperIndex = minOf(lowerIndex + 1, sourceAmplitudes.size - 1)
            val lerpFactor = sourcePosition - lowerIndex

            // Tính toán biên độ chính xác tại điểm i
            val amplitude = (1f - lerpFactor) * sourceAmplitudes[lowerIndex] + lerpFactor * sourceAmplitudes[upperIndex]

            // Tính toán chiều cao thực tế của cột
            val barHeight = height * amplitude
            val x = i * totalBarWidth
            val y = (height - barHeight) / 2 // Giữ cột luôn căn giữa theo chiều dọc

            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2) // Bo tròn hoàn hảo 2 đầu cột
            )
        }
    }
}