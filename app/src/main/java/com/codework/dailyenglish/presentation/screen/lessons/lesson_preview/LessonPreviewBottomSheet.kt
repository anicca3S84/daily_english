package com.codework.dailyenglish.presentation.screen.lessons.lesson_preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.lesson_preview.model.LessonPreview
import com.codework.dailyenglish.presentation.components.LoadingType
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.lessons.lesson_preview.mvi.LessonPreviewEvent
import com.codework.dailyenglish.presentation.screen.lessons.lesson_preview.mvi.LessonPreviewState
import com.codework.dailyenglish.presentation.theme.CourseTheme
import com.codework.dailyenglish.presentation.theme.GrayText

@Composable
fun LessonPreviewSheet(
    state: LessonPreviewState,
    theme: CourseTheme,
    onEvent: (LessonPreviewEvent) -> Unit
) {
    val snackBar = remember { SnackbarHostState() }

    StateWrapper(
        uiState = state.previewDataState,
        modifier = Modifier.fillMaxWidth(),
        snackbarHostState = snackBar,
        loadingType = LoadingType.CIRCULAR,
        onRetry = { onEvent(LessonPreviewEvent.OnRefresh) }
    ) { previewData ->
        previewData?.let { data ->
            LessonPreviewMainContent(
                preview = data,
                theme = theme,
                state = state,
                onStartClick = { id ->
                    onEvent(LessonPreviewEvent.OnStartLearningClick(id))
                }
            )
        }
    }
}

@Composable
private fun LessonPreviewMainContent(
    preview: LessonPreview,
    theme: CourseTheme,
    state: LessonPreviewState,
    onStartClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            // 🔥 HEADER (KHÔNG SCROLL)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp)
            ) {
                LessonHeader(preview, theme)

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = GrayText.copy(alpha = 0.1f)
                )
            }

            // 🔥 CONTENT SCROLL
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {

                SectionTitle(
                    icon = Icons.Rounded.MenuBook,
                    title = "Lesson Content",
                    theme = theme
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = preview.content.ifBlank { "No content available" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .drawBehind {
                            drawLine(
                                color = theme.primaryColor.copy(alpha = 0.3f),
                                start = Offset(0f, 0f),
                                end = Offset(0f, size.height),
                                strokeWidth = 3.dp.toPx()
                            )
                        }
                        .padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                if (preview.outcomes.isNotEmpty()) {
                    SectionTitle(
                        icon = Icons.Rounded.CheckCircle,
                        title = "What you'll learn",
                        theme = theme
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        preview.outcomes.forEach {
                            OutcomeItem(text = it)
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }

                SectionTitle(
                    icon = Icons.Rounded.HistoryEdu,
                    title = "Practice Skills",
                    theme = theme
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (preview.hasVocab) SkillTag("Vocabulary", Icons.Rounded.MenuBook, theme)
                    if (preview.hasGrammar) SkillTag("Grammar", Icons.Rounded.Edit, theme)
                    if (preview.hasListening) SkillTag("Listening", Icons.Rounded.Headset, theme)
                    if (preview.hasSpeaking) SkillTag("Speaking", Icons.Rounded.Mic, theme)
                }

                Spacer(modifier = Modifier.height(140.dp)) // tránh bị che bởi button
            }
        }

        // 🔥 BOTTOM FIXED
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp, top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFFFFC107).copy(alpha = 0.1f), CircleShape)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Rounded.ElectricBolt,
                        null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Complete to earn +${preview.xpReward} XP",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC59100)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(theme.headerColor)
                        .clickable { onStartClick(preview.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Start Learning Now",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonHeader(preview: LessonPreview, theme: CourseTheme) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(18.dp),
            color = theme.primaryColor.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Rounded.AutoAwesome,
                    null,
                    tint = theme.primaryColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                preview.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )

            Text(
                preview.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )
        }
    }
}

@Composable
private fun SectionTitle(icon: ImageVector, title: String, theme: CourseTheme) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = theme.primaryColor, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun OutcomeItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Rounded.Check,
            null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier
                .size(20.dp)
                .background(Color(0xFF4CAF50).copy(0.1f), CircleShape)
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SkillTag(label: String, icon: ImageVector, theme: CourseTheme) {
    Surface(
        shape = CircleShape,
        color = theme.primaryColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, theme.primaryColor.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = theme.primaryColor, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = theme.primaryColor
            )
        }
    }
}