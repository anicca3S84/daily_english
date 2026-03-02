package com.codework.daily_english.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.MainGradient

@Composable
fun QuizVocabularyCompleteDialog(
    score: Int,
    total: Int,
    onContinue: () -> Unit,
    onRetryQuiz: () -> Unit,
    onRelearnWords: () -> Unit,
    onDismiss: () -> Unit,
    // Thêm tham số để tùy chỉnh text nút tiếp theo
    nextButtonText: String = "Continue to Listening"
) {
    val percentageRate = if (total > 0) score.toFloat() / total else 0f
    val percentValue = if (total > 0) (score * 100) / total else 0
    val isPerfect = percentageRate >= 1f
    val isPassed = percentageRate >= 0.7f

    val titleMsg = when {
        isPerfect -> "Awesome! 🎉"
        isPassed -> "Good Job!"
        else -> "Keep Trying"
    }

    val subMsg = when {
        isPerfect -> "You've perfectly completed the review."
        isPassed -> "You passed!\nBut a quick review wouldn't hurt."
        else -> "You need a bit more practice\nto master these."
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ==========================================
                // HEADER (ICON & TITLE)
                // ==========================================
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFFBEB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = titleMsg,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BluePrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ==========================================
                // SUBTITLE
                // ==========================================
                Text(
                    text = subMsg,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ==========================================
                // SCORE BOARD
                // ==========================================
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "SCORE",
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$score / $total",
                            style = TextStyle(
                                brush = MainGradient,
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color(0xFFCBD5E1))
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ACCURACY",
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${percentValue}%",
                            style = TextStyle(
                                brush = MainGradient,
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ==========================================
                // BUTTONS
                // ==========================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when {
                        isPerfect -> {
                            // Sử dụng text động được truyền vào
                            PrimaryButton(text = nextButtonText, onClick = onContinue)
                        }
                        isPassed -> {
                            PrimaryButton(text = nextButtonText, onClick = onContinue)
                            SecondaryGradientButton(text = "Retry Quiz", onClick = onRetryQuiz)
                        }
                        else -> {
                            PrimaryButton(text = "Retry Quiz", onClick = onRetryQuiz)
                            SecondaryGradientButton(text = "Review Vocabulary", onClick = onRelearnWords)
                        }
                    }
                }
            }
        }
    }
}