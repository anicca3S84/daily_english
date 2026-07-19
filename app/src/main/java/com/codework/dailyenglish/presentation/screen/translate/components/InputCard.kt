package com.codework.dailyenglish.presentation.screen.translate.components

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop // Import nút dừng ghi âm
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.components.AppCircleButton
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun InputCard(
    text: String,
    hint: String,
    isRecording: Boolean, // 1. Bổ sung tham số nhận trạng thái ghi âm từ Screen
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onPlayAudio: () -> Unit,
    onTranslate: () -> Unit,
    onImageClick: () -> Unit,
    onCameraClick: () -> Unit,
    onMicClick: () -> Unit
) {
    // 2. Tạo hiệu ứng phập phồng (Pulse) khi đang ghi âm
    val infiniteTransition = rememberInfiniteTransition(label = "PulseTransition")
    val pulseScale by if (isRecording) {
        infiniteTransition.animateFloat(
            initialValue = 1.0f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "ScaleAnimation"
        )
    } else {
        remember { mutableStateOf(1.0f) }
    }

    // 3. Hiệu ứng chuyển màu mượt mà giữa màu xám nhạt mặc định và màu đỏ khi đang nói
    val startBgColor by animateColorAsState(
        targetValue = if (isRecording) Color(0xFFEF4444) else Color(0xFFF2F4F7),
        animationSpec = tween(300),
        label = "StartColor"
    )

    val endBgColor by animateColorAsState(
        targetValue = if (isRecording) Color(0xFFDC2626) else Color(0xFFF2F4F7),
        animationSpec = tween(300),
        label = "EndColor"
    )

    val micBrush = Brush.linearGradient(listOf(startBgColor, endBgColor))

    Card(
        shape = RoundedCornerShape(Spaces.Space24),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = Spaces.Space4),
        border = BorderStroke(Spaces.Space1, AppColors.BorderStrokeColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(Spaces.Space200)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(Spaces.Space20)
            ) {
                // 4. Thay đổi nội dung gợi ý động tùy thuộc vào việc người dùng có đang ghi âm hay không
                if (text.isEmpty()) {
                    if (isRecording) {
                        Text(
                            text = "Listening...",
                            color = Color(0xFFEF4444),
                            fontSize = AppTextSizes.TextSize20,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            text = hint,
                            color = Color.LightGray,
                            fontSize = AppTextSizes.TextSize20
                        )
                    }
                }

                BasicTextField(
                    value = text,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(fontSize = AppTextSizes.TextSize20, color = Color.Black),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onTranslate()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = Spaces.Space50)
                )

                if (text.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .width(Spaces.Space40),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spaces.Space8)
                    ) {
                        AppCircleButton(icon = Icons.Default.Close, onClick = onClear)
                        AppCircleButton(icon = Icons.AutoMirrored.Filled.VolumeUp, onClick = onPlayAudio)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spaces.Space16, vertical = Spaces.Space12),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spaces.Space12)
                ) {
//                    AppCircleButton(icon = Icons.Default.Image, onClick = onImageClick)
//                    AppCircleButton(icon = Icons.Default.CameraAlt, onClick = onCameraClick)

                    // 5. Nút Mic được tùy biến lại với hiệu ứng hoạt ảnh phập phồng (Pulse) và đổi màu
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = pulseScale
                                scaleY = pulseScale
                            }
                            .size(Spaces.Space40)
                            .clip(CircleShape)
                            .background(brush = micBrush)
                            .clickable { onMicClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = "Mic Action",
                            tint = if (isRecording) Color.White else Color.Gray,
                            modifier = Modifier.size(Spaces.Space24)
                        )
                    }
                }
            }
        }
    }
}