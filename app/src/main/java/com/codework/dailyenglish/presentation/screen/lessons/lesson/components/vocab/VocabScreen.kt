package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.vocab

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun VocabCard(
    data: StepData.Vocab,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onPlayAudio:  (String?, String, Float) -> Unit = { _,_, _ -> }
) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500),
        label = "flip"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spaces.Space32, vertical = Spaces.Space16),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Nhãn tiêu đề
            Text(
                text = "Từ vựng mới",
                fontSize = AppTextSizes.TextSizeTitle,
                fontWeight = FontWeight.ExtraBold,
                color = AppColors.Slate700,
                modifier = Modifier.padding(bottom = Spaces.Space24)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spaces.Space16)
                    .height(Spaces.Space475)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 16f * density
                    }
                    .border(Spaces.Space1, Color(0xFFF1F5F9), RoundedCornerShape(AppRadius.Radius32))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // Tắt hiệu ứng ripple khi lật thẻ
                    ) { isFlipped = !isFlipped },
                shape = RoundedCornerShape(AppRadius.Radius32),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(Spaces.Space3)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (rotation <= 90f) {
                        VocabCardFront(
                            data = data,
                            isFavorite = isFavorite,
                            onToggleFavorite = onToggleFavorite,
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationY = 180f
                                }
                        ) {
                            VocabCardBack(
                                data = data,
                                isFavorite = isFavorite,
                                onToggleFavorite = onToggleFavorite,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spaces.Space36))

            // CỤM 3 NÚT ACTION 3D BÊN DƯỚI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút Ốc Sên (Phát chậm)
                SnailAudioButton3D(
                    onClick = { onPlayAudio(data.usAudioUrl, data.word, 0.5f) }
                )

                Spacer(modifier = Modifier.width(Spaces.Space24))

                // Nút Loa Chính (Nổi bật hơn, màu xanh)
                MainAudioButton3D(
                    onClick = { onPlayAudio(data.usAudioUrl, data.word, 1f) }
                )

                Spacer(modifier = Modifier.width(Spaces.Space24))

                // Nút Lưu từ (Bookmark) - Đổi màu vàng khi isFavorite = true
                BookmarkButton3D(
                    isSaved = isFavorite,
                    onClick = onToggleFavorite
                )
            }
        }
    }
}

// ==========================================
// CÁC COMPONENT NÚT BẤM 3D (NEUMORPHISM)
// ==========================================

@Composable
fun SnailAudioButton3D(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Animation lún xuống
    val yOffset by animateDpAsState(targetValue = if (isPressed) 4.dp else 0.dp, label = "yOffset")

    Box(
        modifier = Modifier
            .size(64.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Tắt Ripple mặc định
                onClick = onClick
            )
    ) {
        // Lớp Shadow (Đáy)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 4.dp)
                .background(Color(0xFFE5E5E5), CircleShape)
        )
        // Lớp Nổi (Mặt trên)
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = yOffset),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(2.dp, Color(0xFFE5E5E5))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "🐌", fontSize = AppTextSizes.TextSize24)
            }
        }
    }
}

@Composable
fun MainAudioButton3D(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset by animateDpAsState(targetValue = if (isPressed) 4.dp else 0.dp, label = "yOffset")

    Box(
        modifier = Modifier
            .size(72.dp) // Nút chính to hơn một chút
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // Lớp Shadow (Xanh đậm)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 4.dp)
                .background(Color(0xFF1899D6), CircleShape)
        )
        // Lớp Nổi (Xanh sáng)
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = yOffset),
            shape = CircleShape,
            color = Color(0xFF1CB0F6),
            border = BorderStroke(2.dp, Color(0xFF1899D6))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.VolumeUp,
                    contentDescription = "Nghe",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun BookmarkButton3D(isSaved: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset by animateDpAsState(targetValue = if (isPressed) 4.dp else 0.dp, label = "yOffset")

    // Animation chuyển màu mượt mà khi nhấn lưu
    val bgColor by animateColorAsState(targetValue = if (isSaved) Color(0xFFFEF3C7) else Color.White, label = "bg")
    val borderColor by animateColorAsState(targetValue = if (isSaved) Color(0xFFF4B844) else Color(0xFFE5E5E5), label = "border")
    val iconColor by animateColorAsState(targetValue = if (isSaved) Color(0xFFF4B844) else Color(0xFF9CA3AF), label = "icon")

    Box(
        modifier = Modifier
            .size(64.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // Lớp Shadow (Đáy đổi màu theo trạng thái)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 4.dp)
                .background(borderColor, CircleShape)
        )
        // Lớp Nổi
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = yOffset),
            shape = CircleShape,
            color = bgColor,
            border = BorderStroke(2.dp, borderColor)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Lưu từ",
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}