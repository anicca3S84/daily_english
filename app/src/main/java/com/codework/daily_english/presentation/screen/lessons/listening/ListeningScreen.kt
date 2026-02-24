package com.codework.daily_english.presentation.screen.lessons.listening

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.daily_english.presentation.components.AppTopBar
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.PrimaryButton
import com.codework.daily_english.presentation.components.ProgressTopBar
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.DialogueLine
import com.codework.daily_english.presentation.screen.lessons.review.QuizReviewEvent
import com.codework.daily_english.presentation.theme.*

@Composable
fun ListeningScreen(
    state: UiState<ListeningData>,
    onEvent: (ListeningEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
        topBar = {
            // ĐÃ THAY PROGRESS BẰNG APPTOPBAR
            AppTopBar(
                onBackClick = onBackClick,
                titleContent = {
                    Text(
                        text = "Listening Practice",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                },
                onMenuClick = null,
            )
        },

        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 16.dp)
            ) {
                PrimaryButton(
                    text = "Let's Practice!",
                    onClick = { onEvent(ListeningEvent.OnLetsPracticeClick) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    ) { paddingValues ->
        BaseScreen(state = state) { data ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    // --- BADGE ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(HintBackGround)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = "🎧", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "DIALOGUE",
                            color = BluePrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- ẢNH MINH HỌA ---
                    AsyncImage(
                        model = data.imageUrl,
                        contentDescription = "Dialogue Context",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = data.title,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- AUDIO PLAYER CARD ---
                    AudioPlayerCard(data = data, onEvent = onEvent)

                    Spacer(modifier = Modifier.height(32.dp))
                }

                // --- TRANSCRIPT (KỊCH BẢN CHỮ) ---
                items(data.dialogues) { line ->
                    // Kiểm tra xem dòng hội thoại này có đang được phát hay không
                    val isActive = data.currentTime in line.startTime..line.endTime

                    TranscriptRow(line = line, isActive = isActive)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

// --- SUB COMPONENTS ---

@Composable
fun AudioPlayerCard(data: ListeningData, onEvent: (ListeningEvent) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Play/Pause Button
                Surface(
                    shape = CircleShape,
                    color = BluePrimary,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onEvent(ListeningEvent.TogglePlayPause) }
                ) {
                    Icon(
                        imageVector = if (data.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Progress Bar
                Slider(
                    value = if (data.duration > 0) data.currentTime.toFloat() / data.duration else 0f,
                    onValueChange = { onEvent(ListeningEvent.OnSeek(it)) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = BluePrimary,
                        activeTrackColor = BluePrimary,
                        inactiveTrackColor = Color(0xFFE2E8F0)
                    )
                )

                // Slow Speed Button (Turtle)
                Surface(
                    shape = CircleShape,
                    color = if (data.isSlowMode) HintBackGround else Color.Transparent,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onEvent(ListeningEvent.ToggleSpeed) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "🐢",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }

            // Time Texts
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 64.dp, end = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(data.currentTime),
                    color = GrayText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatTime(data.duration),
                    color = GrayText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TranscriptRow(line: DialogueLine, isActive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isActive) HintBackGround else Color.Transparent)
            .padding(12.dp),
        verticalAlignment = Alignment.Top // SỬA TẠI ĐÂY LÀ OK NHÉ!
    ) {
        // Cột hiển thị Emoji nhân vật
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = line.speakerEmoji, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Cột Tên và Nội dung thoại
        Column {
            Text(
                text = line.speakerName,
                color = if (isActive) BluePrimary else GrayText,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = line.text,
                color = if (isActive) TextPrimary else GrayText.copy(alpha = 0.8f),
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
    }
}

// Hàm phụ trợ format mili-giây sang mm:ss
fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}