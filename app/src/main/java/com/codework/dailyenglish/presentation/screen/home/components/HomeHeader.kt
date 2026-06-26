package com.codework.dailyenglish.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Sử dụng Coil để load ảnh từ URL
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun HomeHeader(
    name: String,
    avatarUrl: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Avatar tròn và nhỏ hơn
        AsyncImage(
            model = avatarUrl ?: "",
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(46.dp) // Kích thước nhỏ gọn
                .clip(CircleShape), // Hình tròn
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 2. Nội dung Text (Màu xanh chủ đạo)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                color = AppColors.BluePrimary, // Màu xanh của app
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Are you ready for focus?",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextSub
            )
        }

        Box(
            modifier = Modifier.padding(end = 4.dp, top = 4.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            // 1. Icon thông báo (To hơn và màu tối hiện đại)
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = AppColors.Slate800, // Màu xám đen sang trọng
                modifier = Modifier.size(36.dp) // Tăng kích thước icon lên 36dp
            )

            // 2. Badge thông báo
            Box(
                modifier = Modifier
                    .size(20.dp) // Badge cũng to hơn một chút cho cân đối
                    .background(AppColors.Rose500, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "1",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    // Thêm offset để căn chỉnh lại nếu font chữ bị lệch so với tâm hình tròn
                    modifier = Modifier.offset(y = (-0.5).dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}