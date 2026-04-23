package com.codework.dailyenglish.presentation.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.theme.AppBackground
import com.codework.dailyenglish.presentation.theme.GrayText
import com.codework.dailyenglish.presentation.theme.MainGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppProgressTopBar(
    progress: Float,
    progressText: String,
    onBackClick: () -> Unit,
    onCloseClick: (() -> Unit)? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "progressAnimation"
    )

    Surface(
        color = AppBackground,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút Back tròn trịa phong cách Pro
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

            // Thanh Progress Bar tùy chỉnh
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp) // Chỉnh lại độ cao cho thanh thoát
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFE5E7EB))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress) // Dùng giá trị đã animate
                        .fillMaxHeight()
                        .background(MainGradient) // Sử dụng Gradient xịn của bạn
                        .clip(RoundedCornerShape(5.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Hiển thị nút Close hoặc Text tiến độ
            if (onCloseClick != null) {
                IconButton(onClick = onCloseClick, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, null, tint = GrayText)
                }
            } else {
                Text(
                    text = progressText, // Dùng text truyền từ State
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}