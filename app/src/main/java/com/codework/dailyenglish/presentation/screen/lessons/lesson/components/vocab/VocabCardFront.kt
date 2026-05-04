package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.vocab

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.R
@Composable
fun VocabCardFront(
    data: StepData.Vocab,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
) {
    // Hiệu ứng nhấp nháy cho text "Chạm để lật"
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spaces.Space24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🚀",
                fontSize = 80.sp,
                modifier = Modifier.padding(bottom = Spaces.Space32)
            )

            Text(
                text = data.word,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF4B4B4B),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(Spaces.Space8))

            if (!data.usPhonetic.isNullOrEmpty()) {
                Text(
                    text = "${data.usPhonetic} (adj)",
                    fontSize = 20.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Hint lật thẻ
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = Spaces.Space24),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(pulseAlpha) // Áp dụng animation nhấp nháy
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(Spaces.Space20)
                )

                Spacer(modifier = Modifier.width(Spaces.Space8))

                Text(
                    text = stringResource(R.string.label_tap_to_flip),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
    }
}