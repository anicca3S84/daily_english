// FILE: presentation/screen/home/components/HomeHeader.kt
package com.codework.dailyenglish.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.MilitaryTech // Sử dụng biểu tượng vương miện/huy chương có sẵn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun HomeHeader(
    name: String,
    avatarUrl: String?,
    isPremium: Boolean,
    onPremiumClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = avatarUrl ?: "",
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = AppColors.BluePrimary,
                    fontWeight = FontWeight.Bold
                )
                // Nếu đã nâng cấp VIP, đính kèm luôn một cái Badge nhỏ cạnh tên cho uy tín
                if (isPremium) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFD700), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text("PRO", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
            }
            Text(
                text = "Are you ready for focus?",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextSub
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // NÚT VƯƠNG MIỆN PREMIUM 3D
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPremium) Color(0xFFFFF9E6) else AppColors.Slate100
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // Tắt hiệu ứng mặc định để giữ chuẩn 3D của dự án
                    ) {
                        onPremiumClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.MilitaryTech, // Render hình tượng vương miện/quyền lực
                    contentDescription = "Premium Status",
                    tint = if (isPremium) Color(0xFFFFB300) else Color.Gray, // Phát sáng vàng nếu là VIP
                    modifier = Modifier.size(28.dp)
                )
            }

            // NÚT THÔNG BÁO GIỮ NGUYÊN LOGIC CŨ
            Box(
                modifier = Modifier.padding(end = 4.dp, top = 4.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = AppColors.Slate800,
                    modifier = Modifier.size(36.dp)
                )

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(AppColors.Rose500, CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.offset(y = (-0.5).dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}