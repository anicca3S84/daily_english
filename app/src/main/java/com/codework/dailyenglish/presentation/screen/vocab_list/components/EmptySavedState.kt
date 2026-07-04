package com.codework.dailyenglish.presentation.screen.vocab_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes


@Composable
fun EmptySavedState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Căn giữa tuyệt đối mọi thứ bên trong Box
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(100.dp), // Tăng nhẹ kích thước Icon cho cân xứng
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your library is empty",
                fontSize = 20.sp, // Tăng size chữ tiêu đề chính lên hẳn 20.sp (hoặc tùy chỉnh)
                fontWeight = FontWeight.Bold, // Đổi sang Bold cho nổi bật
                color = AppColors.TextMain // Dùng màu text đậm hơn thay vì Gray để dễ đọc
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Save words you want to review later!",
                fontSize = 15.sp, // Tăng size chữ mô tả lên 15.sp
                fontWeight = FontWeight.Normal,
                color = Color.Gray, // Đổi từ LightGray sang Gray để tăng độ tương phản
                textAlign = TextAlign.Center
            )
        }
    }
}