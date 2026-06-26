package com.codework.dailyenglish.presentation.screen.ai.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun ModernInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isEnabled: Boolean,
    onAttachClick: () -> Unit,
    onMicClick: () -> Unit
) {
    val inputBackgroundColor = Color(0xFFF2F4F7)
    val isTyping = value.isNotBlank()

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // Nút đính kèm ảnh
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(inputBackgroundColor).clickable { onAttachClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, "Add", tint = Color.Gray, modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Khung nhập liệu
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            color = inputBackgroundColor,
            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 16.sp, color = Color(0xFF263238), lineHeight = 24.sp),
                maxLines = 5,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        if (value.isEmpty()) { Text("Message Anicca...", color = Color.Gray, fontSize = 16.sp) }
                        innerTextField()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Nút Send / Mic
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(brush = if (isTyping) AppColors.MainGradient else Brush.linearGradient(listOf(Color(0xFF212121), Color(0xFF212121))))
                .clickable(enabled = isEnabled, onClick = { if (isTyping) onSend() else onMicClick() }),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isTyping) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                contentDescription = if (isTyping) "Send" else "Voice",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}