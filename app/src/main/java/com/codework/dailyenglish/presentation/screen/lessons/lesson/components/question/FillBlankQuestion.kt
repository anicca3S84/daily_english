package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.FillBlankInputArea
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun FillBlankQuestion(
    prompt: String,
    sentenceWithBlank: String,
    onPlayAudio: (String?, String, Float) -> Unit,
    hintMask: List<String?>,
    initialAnswer: String? = null,
    onAnswerSelected: (String) -> Unit,
    isCorrect: Boolean? = null,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.AppBackground)
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Fill in the Blank",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1F2937),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .clickable { onPlayAudio(null, prompt, 1f) },
                color = Color(0xFFEFF6FF),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.VolumeUp,
                        contentDescription = "Nghe",
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = prompt,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )

            if (sentenceWithBlank.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = sentenceWithBlank,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        FillBlankInputArea(
            hintMask = hintMask,
            initialAnswer = initialAnswer,
            onAnswerSelected = onAnswerSelected,
            isCorrect = isCorrect
        )
    }
}