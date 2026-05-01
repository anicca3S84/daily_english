package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.vocab

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
    onPlayAudio: (String, Float) -> Unit = { _, _ -> }
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spaces.Space100 * 5 + Spaces.Space60) // 560.dp
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 16f * density
                    }
                    .border(Spaces.Space1, Color(0xFFF1F5F9), RoundedCornerShape(AppRadius.Radius32))
                    .clickable { isFlipped = !isFlipped },
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
                            onPlayAudio = onPlayAudio
                        )
                    } else {
                        VocabCardBack(
                            data = data,
                            isFavorite = isFavorite,
                            onToggleFavorite = onToggleFavorite,
                            modifier = Modifier.graphicsLayer {
                                rotationY = 180f
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spaces.Space36))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Spaces.Space30),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { data.usAudioUrl?.let { onPlayAudio(it, 1f) } },
                    modifier = Modifier
                        .size(Spaces.Space64 + Spaces.Space1) // ~65.dp
                        .shadow(Spaces.Space3, CircleShape)
                        .background(Color.White, CircleShape)
                        .border(Spaces.Space1, AppColors.Slate100, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VolumeUp,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(Spaces.Space24 + Spaces.Space2)
                    )
                }

                IconButton(
                    onClick = { data.usAudioUrl?.let { onPlayAudio(it, 0.5f) } },
                    modifier = Modifier
                        .size(Spaces.Space64 + Spaces.Space1)
                        .shadow(Spaces.Space3, CircleShape)
                        .background(Color.White, CircleShape)
                        .border(Spaces.Space1, AppColors.Slate100, CircleShape)
                ) {
                    Text(
                        text = "🐌",
                        fontSize = AppTextSizes.TextSize20
                    )
                }
            }
        }
    }
}