package com.codework.dailyenglish.presentation.screen.ai.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop // Nút ngắt dừng ghi âm
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun ModernInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isEnabled: Boolean,
    isVoiceRecording: Boolean, // Nhận diện trạng thái ghi âm
    onAttachClick: () -> Unit,
    onMicClick: () -> Unit
) {
    val inputBackgroundColor = Color(0xFFF2F4F7)
    val isTyping = value.isNotBlank()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Hiệu ứng phập phồng (Pulse) khi đang ghi âm
    val infiniteTransition = rememberInfiniteTransition(label = "PulseTransition")
    val pulseScale by if (isVoiceRecording) {
        infiniteTransition.animateFloat(
            initialValue = 1.0f,
            targetValue = 1.12f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "ScaleAnimation"
        )
    } else {
        remember { mutableStateOf(1.0f) }
    }

    // Chuyển đổi màu sắc mượt mà giữa các trạng thái
    val startBgColor by animateColorAsState(
        targetValue = when {
            isVoiceRecording -> Color(0xFFEF4444)
            isTyping -> Color(0xFF4A90E2) // Hoặc màu bắt đầu của MainGradient của bạn
            else -> Color(0xFF212121)
        },
        animationSpec = tween(300),
        label = "StartColor"
    )

    val endBgColor by animateColorAsState(
        targetValue = when {
            isVoiceRecording -> Color(0xFFDC2626)
            isTyping -> Color(0xFF50E3C2) // Hoặc màu kết thúc của MainGradient của bạn
            else -> Color(0xFF212121)
        },
        animationSpec = tween(300),
        label = "EndColor"
    )

    val buttonBrush = Brush.linearGradient(listOf(startBgColor, endBgColor))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // Nút đính kèm tệp tin
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(inputBackgroundColor)
                .clickable { onAttachClick() },
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
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (isTyping && isEnabled) {
                            onSend()
                            keyboardController?.hide()
                        }
                    }
                ),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        // Hiển thị gợi ý tùy trạng thái
                        if (value.isEmpty() && !isVoiceRecording) {
                            Text("Message Anicca...", color = Color.Gray, fontSize = 16.sp)
                        } else if (isVoiceRecording && value.isEmpty()) {
                            Text("Listening...", color = Color(0xFFEF4444), fontSize = 16.sp)
                        }
                        innerTextField()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Nút tương tác động (Mic / Stop / Send)
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                }
                .size(48.dp)
                .clip(CircleShape)
                .background(brush = buttonBrush)
                .clickable(
                    enabled = isEnabled,
                    onClick = {
                        if (isVoiceRecording) {
                            onMicClick() // Đang nói nhấn vào sẽ dừng
                        } else if (isTyping) {
                            onSend() // Có chữ nhấn vào sẽ gửi
                            keyboardController?.hide()
                        } else {
                            onMicClick() // Đang rảnh nhấn vào sẽ bật mic
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            val iconVector = when {
                isVoiceRecording -> Icons.Default.Stop
                isTyping -> Icons.AutoMirrored.Filled.Send
                else -> Icons.Default.Mic
            }

            Icon(
                imageVector = iconVector,
                contentDescription = "Action Button",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}