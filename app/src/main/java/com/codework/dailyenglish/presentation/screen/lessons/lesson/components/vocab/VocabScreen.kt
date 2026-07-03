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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.screen.flashcard.components.FlashcardBackFace
import com.codework.dailyenglish.presentation.screen.flashcard.components.FlashcardFrontFace
import com.codework.dailyenglish.presentation.screen.flashcard.components.MainAudioButton3D
import com.codework.dailyenglish.presentation.screen.flashcard.components.SnailAudioButton3D
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
    onPlayAudio: (String?, String, Float) -> Unit = { _, _, _ -> }
) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500),
        label = "flip"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spaces.Space32, vertical = Spaces.Space16),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Từ vựng mới",
            fontSize = AppTextSizes.TextSizeTitle,
            fontWeight = FontWeight.ExtraBold,
            color = AppColors.Slate700,
            modifier = Modifier.padding(bottom = Spaces.Space16)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Giữ nguyên weight để tự co giãn theo màn hình
                .padding(horizontal = Spaces.Space16)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 14f * density // Đồng bộ khoảng cách camera (14 thay vì 16)
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { isFlipped = !isFlipped },
            shape = RoundedCornerShape(24.dp), // Đồng bộ bo góc 24.dp giống màn hình trước
            colors = CardDefaults.cardColors(
                containerColor = AppColors.FlashCardBackground // <--- Đổi thành màu nền Flashcard của bạn ở đây!
            ),
            border = BorderStroke(
                width = 1.dp,
                color = AppColors.FlashCardBorder // Đồng bộ màu viền border của bài trước
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Đồng bộ elevation 2.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (rotation <= 90f) {
                    FlashcardFrontFace(
                        word = data.word,
                        phonetic = data.ukPhonetic ?: ""
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                rotationY = 180f
                            }
                    ) {
                        data.examples?.let {
                            FlashcardBackFace(
                                meaning = data.meaning,
                                examples = data.examples
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Spaces.Space24))

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