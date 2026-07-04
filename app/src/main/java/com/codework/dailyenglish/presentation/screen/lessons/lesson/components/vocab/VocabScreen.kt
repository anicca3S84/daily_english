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
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.BookmarkButton3D
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

    var localIsFavorite by remember(data.word, isFavorite) { mutableStateOf(isFavorite) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spaces.Space32, vertical = Spaces.Space16),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "New vocabulary",
            fontSize = AppTextSizes.TextSizeTitle,
            fontWeight = FontWeight.ExtraBold,
            color = AppColors.Slate700,
            modifier = Modifier.padding(bottom = Spaces.Space16)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = Spaces.Space16)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 14f * density
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { isFlipped = !isFlipped },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.FlashCardBackground
            ),
            border = BorderStroke(
                width = 1.dp,
                color = AppColors.FlashCardBorder
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                isSaved = localIsFavorite,
                onClick = {
                    localIsFavorite = !localIsFavorite
                    onToggleFavorite()
                }
            )
        }

        Spacer(modifier = Modifier.height(Spaces.Space24))

    }
}

