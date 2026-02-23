package com.codework.daily_english.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.daily_english.presentation.theme.AppBackground
import com.codework.daily_english.presentation.theme.GrayText
import com.codework.daily_english.presentation.theme.MainGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressTopBar(
    currentStep: Int,
    totalSteps: Int,
    onBackClick: () -> Unit,
    onCloseClick: (() -> Unit)? = null // Tùy chọn: Đôi khi bạn muốn nút đóng thay vì menu
) {
    // Sử dụng Surface để có màu nền và đổ bóng chuẩn
    Surface(
        color = AppBackground,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 0.dp // Hoặc tăng lên nếu muốn có bóng
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding() // Tránh tai thỏ
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- 1. NÚT BACK (Custom Style) ---
            Surface(
                onClick = onBackClick,
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF374151),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // --- 2. THANH PROGRESS (Co giãn linh hoạt) ---
            Box(
                modifier = Modifier
                    .weight(1f) // Chiếm hết không gian ở giữa
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFE5E7EB))
            ) {
                val progress = if (totalSteps > 0) currentStep / totalSteps.toFloat() else 0f

                // Thanh màu gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(MainGradient)
                        .clip(RoundedCornerShape(6.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // --- 3. TEXT CHỈ SỐ HOẶC NÚT ĐÓNG ---
            if (onCloseClick != null) {
                // Nếu có action Close (ví dụ trong bài Quiz)
                IconButton(onClick = onCloseClick, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, null, tint = GrayText)
                }
            } else {
                // Mặc định hiện chỉ số text (1/10)
                Text(
                    text = "$currentStep/$totalSteps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}