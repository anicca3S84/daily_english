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

    // Đổi Box thành Column và cho nó fillMaxSize để chiếm trọn không gian cho phép
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spaces.Space32, vertical = Spaces.Space16),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // CỤM 1: Nhãn tiêu đề (Header)
        Text(
            text = "Từ vựng mới",
            fontSize = AppTextSizes.TextSizeTitle,
            fontWeight = FontWeight.ExtraBold,
            color = AppColors.Slate700,
            modifier = Modifier.padding(bottom = Spaces.Space16) // Giảm một chút cho gọn
        )

        // CỤM 2: Thẻ Flashcard (Dùng weight(1f) để nó tự động giãn dài/ngắn tuỳ theo màn hình)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // <--- QUAN TRỌNG: Đây chính là chiếc lò xo
                .padding(horizontal = Spaces.Space16)
                // ĐÃ XÓA: .height(Spaces.Space475) vì mình muốn nó tự động co giãn
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 16f * density
                }
                .border(Spaces.Space1, Color(0xFFF1F5F9), RoundedCornerShape(AppRadius.Radius32))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
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

        // CỤM 3: Khoảng cách và Hàng nút bấm (Footer)
        Spacer(modifier = Modifier.height(Spaces.Space24)) // Cố định khoảng cách nút và thẻ

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SnailAudioButton3D(
                onClick = { onPlayAudio(data.usAudioUrl, data.word, 0.5f) }
            )

            Spacer(modifier = Modifier.width(Spaces.Space24))

            MainAudioButton3D(
                onClick = { onPlayAudio(data.usAudioUrl, data.word, 1f) }
            )

            Spacer(modifier = Modifier.width(Spaces.Space24))

            BookmarkButton3D(
                isSaved = isFavorite,
                onClick = onToggleFavorite
            )
        }
    }
}

@Composable
fun SnailAudioButton3D(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset by animateDpAsState(targetValue = if (isPressed) 4.dp else 0.dp, label = "yOffset")

    Box(
        modifier = Modifier
            .size(64.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 4.dp)
                .background(Color(0xFFE5E5E5), CircleShape)
        )
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
            .size(72.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 4.dp)
                .background(Color(0xFF1899D6), CircleShape)
        )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 4.dp)
                .background(borderColor, CircleShape)
        )
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