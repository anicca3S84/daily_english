package com.codework.dailyenglish.presentation.screen.ai.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun EnhancedMessageBubble(
    message: ChatMessage,
    showFeatures: Boolean = false, // Biến check ẩn/hiện icon cho màn Agent
    isTranslated: Boolean = false,
    displayedText: String = message.text,
    onTranslateClick: () -> Unit = {},
    onSpeakClick: () -> Unit = {}
) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom // Căn lề dưới để bong bóng chat và icon luôn thẳng hàng đáy
    ) {
        if (isUser) {
            // Tin nhắn người dùng (Nằm bên phải, không hiển thị nút chức năng)
            BubbleContent(isUser = true, text = displayedText)
        } else {
            // Tin nhắn AI (Nằm bên trái)
            Row(
                modifier = Modifier.fillMaxWidth(0.88f), // Giới hạn chiều rộng tối đa (88%) để chừa khoảng trống bên phải màn hình
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                // 1. Bong bóng chat AI (Tự động co giãn theo độ dài chữ nhờ weight(..., fill = false))
                Box(modifier = Modifier.weight(1f, fill = false)) {
                    BubbleContent(isUser = false, text = displayedText)
                }

                // 2. Các nút Action đặt bên cạnh bubble
                if (showFeatures) {
                    Row(
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MessageActionChip(
                            imageVector = Icons.Rounded.Translate,
                            isSelected = isTranslated,
                            onClick = onTranslateClick
                        )
                        MessageActionChip(
                            imageVector = Icons.Rounded.VolumeUp,
                            isSelected = false,
                            onClick = onSpeakClick
                        )
                    }
                }
            }
        }
    }
}

/**
 * Phần nội dung chính của Bong bóng Chat
 */
@Composable
private fun BubbleContent(isUser: Boolean, text: String) {
    Surface(
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = if (isUser) 20.dp else 4.dp,
            bottomEnd = if (isUser) 4.dp else 20.dp
        ),
        color = Color.Transparent,
        shadowElevation = if (isUser) 4.dp else 1.dp
    ) {
        Box(
            modifier = Modifier.background(
                brush = if (isUser) AppColors.MainGradient
                else Brush.linearGradient(listOf(Color.White, Color.White))
            )
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(12.dp),
                color = if (isUser) Color.White else Color(0xFF333333),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Phím chức năng siêu tối giản dạng tròn (Action Chip)
 */
@Composable
private fun MessageActionChip(
    imageVector: ImageVector,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                if (isSelected) Color(0xFFE0F2FE) // Nền xanh nhạt dễ chịu khi kích hoạt dịch
                else Color(0xFFF1F5F9) // Nền xám Slate cực nhẹ mặc định
            )
            .clickable(onClick = onClick)
            .padding(8.dp), // Diện tích chạm thoải mái
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = if (isSelected) Color(0xFF0284C7) // Màu xanh dương hiện đại khi kích hoạt
            else Color(0xFF64748B), // Màu xám trung tính mặc định
            modifier = Modifier.size(15.dp) // Icon nhỏ gọn, tinh tế
        )
    }
}