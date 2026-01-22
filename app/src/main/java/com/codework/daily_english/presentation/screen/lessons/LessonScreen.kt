package com.codework.daily_english.presentation.screen.lessons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.screen.courses.CourseData

enum class LessonStepType { VOCABULARY, DIALOGUE, SPEAKING_AI, QUIZ }
enum class StepStatus { LOCKED, UNLOCKED, COMPLETED }

data class LessonStep(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: LessonStepType,
    val status: StepStatus
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    state: UiState<LessonData>,
    onEvent: (LessonEvent) -> Unit,
    onBackClick: () -> Unit,
) {

    // Mock Data cho UI (Sau này sẽ lấy từ ViewModel)
    val steps = listOf(
        LessonStep("1", "Key Vocabulary", "Learn 5 new words", LessonStepType.VOCABULARY, StepStatus.COMPLETED),
        LessonStep("2", "Dialogue", "Ordering at a cafe", LessonStepType.DIALOGUE, StepStatus.UNLOCKED),
        LessonStep("3", "Roleplay with AI", "Practice speaking", LessonStepType.SPEAKING_AI, StepStatus.LOCKED),
        LessonStep("4", "Final Quiz", "Test your knowledge", LessonStepType.QUIZ, StepStatus.LOCKED)
    )

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        BaseScreen(state = state) { data -> // data ở đây là LessonDetail tổng quan
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 1. HEADER ẤN TƯỢNG
                item {
                    LessonHeaderSection(
                        title = data.title,
                        description = "In this lesson, you will learn how to order drinks and snacks.",
                        progress = 0.25f
                    )
                }

                itemsIndexed(steps) { index, step ->
                    LessonPathItem(
                        step = step,
                        isLast = index == steps.lastIndex,
                        onClick = { onEvent(LessonEvent.OnStepClick(step.id)) }
                    )
                }

                // Padding dưới cùng
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}


@Composable
fun LessonHeaderSection(title: String, description: String, progress: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        // Ảnh minh họa nhỏ hoặc Icon chủ đề
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "☕", // Hoặc AsyncImage
                    fontSize = 40.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Thanh tiến độ tổng quan
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color(0xFFE0E0E0)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun LessonPathItem(
    step: LessonStep,
    isLast: Boolean,
    onClick: () -> Unit
) {
    // Màu sắc dựa trên trạng thái
    val (iconColor, ringColor, textColor) = when (step.status) {
        StepStatus.COMPLETED -> Triple(Color.White, Color(0xFF4CAF50), Color.Gray) // Xanh lá
        StepStatus.UNLOCKED -> Triple(Color.White, Color(0xFF2196F3), Color.Black) // Xanh dương
        StepStatus.LOCKED -> Triple(Color(0xFFBDBDBD), Color(0xFFF5F5F5), Color(0xFFBDBDBD)) // Xám
    }

    val iconBgColor = if (step.status == StepStatus.LOCKED) Color(0xFFF5F5F5) else ringColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Quan trọng để vẽ đường kẻ nối
            .clickable(enabled = step.status != StepStatus.LOCKED) { onClick() }
            .padding(horizontal = 24.dp)
    ) {
        // CỘT TRÁI: Icon & Đường kẻ
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(48.dp)
        ) {
            // Vòng tròn Icon
            Surface(
                shape = CircleShape,
                color = iconBgColor,
                modifier = Modifier.size(48.dp),
                shadowElevation = if (step.status == StepStatus.UNLOCKED) 6.dp else 0.dp // Hiệu ứng nổi cho bài đang học
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (step.type) {
                            LessonStepType.VOCABULARY -> Icons.Default.Style // Thẻ bài
                            LessonStepType.DIALOGUE -> Icons.Default.Headphones // Tai nghe
                            LessonStepType.SPEAKING_AI -> Icons.Default.Mic // Mic
                            LessonStepType.QUIZ -> Icons.Default.EmojiEvents // Cúp
                        },
                        contentDescription = null,
                        tint = if (step.status == StepStatus.LOCKED) Color.Gray else Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                    // Nếu đã hoàn thành, hiện dấu tick nhỏ
                    if (step.status == StepStatus.COMPLETED) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(Color.White, CircleShape)
                                .padding(1.dp)
                                .size(16.dp)
                        )
                    }
                }
            }

            // Đường kẻ nối (Line)
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(
                            if (step.status == StepStatus.COMPLETED) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
                        )
                        .padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        // CỘT PHẢI: Nội dung Text
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp) // Căn chỉnh với Icon
                .weight(1f)
        ) {
            Text(
                text = step.type.name.replace("_", " "), // VOCABULARY
                style = MaterialTheme.typography.labelSmall,
                color = ringColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = step.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = step.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = if (step.status == StepStatus.LOCKED) Color.Gray else Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp)) // Khoảng cách giữa các item
        }
    }
}