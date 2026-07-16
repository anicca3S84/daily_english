package com.codework.dailyenglish.presentation.screen.lessons.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.lesson_preview.model.LessonPreview
import com.codework.dailyenglish.presentation.components.LoadingType
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.lessons.preview.components.ObjectiveItem
import com.codework.dailyenglish.presentation.screen.lessons.preview.mvi.LessonPreviewEvent
import com.codework.dailyenglish.presentation.screen.lessons.preview.mvi.LessonPreviewState
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.CourseTheme
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.screen.lessons.preview.components.StatTag
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun LessonPreviewSheet(
    state: LessonPreviewState,
    theme: CourseTheme,
    isCompleted: Boolean,
    onEvent: (LessonPreviewEvent) -> Unit,
    onClose: () -> Unit
) {
    val snackBar = remember { SnackbarHostState() }

    StateWrapper(
        uiState = state.previewDataState,
        modifier = Modifier.fillMaxWidth(),
        snackbarHostState = snackBar,
        loadingType = LoadingType.CIRCULAR,
        onRetry = { onEvent(LessonPreviewEvent.OnRefresh) }
    ) { previewData ->

        Column(
            modifier = Modifier.fillMaxWidth()
                .background(AppColors.AppBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spaces.Space12, end = Spaces.Space16)
            ) {
                Surface(
                    onClick = onClose,
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = Spaces.Space2,
                    modifier = Modifier
                        .size(Spaces.Space36)
                        .align(Alignment.TopEnd)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = AppColors.TextMain,
                            modifier = Modifier.size(Spaces.Space20)
                        )
                    }
                }
            }

            previewData?.let { data ->
                LessonPreviewMainContent(
                    preview = data,
                    theme = theme,
                    state = state,
                    isCompleted = isCompleted,
                    onStartClick = { id ->
                        onEvent(LessonPreviewEvent.OnStartLearningClick(id))
                    },
                    onReviewClick = { id ->
                        onEvent(LessonPreviewEvent.OnReviewClick(id))
                    },
                    onRestartClick = { id ->
                        onEvent(LessonPreviewEvent.OnRestartClick(id))
                    }
                )
            }
        }
    }
}

@Composable
private fun LessonPreviewMainContent(
    preview: LessonPreview,
    theme: CourseTheme,
    state: LessonPreviewState,
    isCompleted: Boolean,
    onStartClick: (String) -> Unit,
    onReviewClick: (String) -> Unit,
    onRestartClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = Spaces.Space24, vertical = Spaces.Space32),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spaces.Space8)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(120.dp)
                    )

                    Text(
                        text = preview.title,
                        color = Color(0xFF1F2937),
                        fontWeight = FontWeight.Black,
                        fontSize = AppTextSizes.TextSize26,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = preview.subtitle,
                        color = Color(0xFF4B5563),
                        textAlign = TextAlign.Center,
                        fontSize = AppTextSizes.TextSizeTitle,
                        lineHeight = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(Spaces.Space24))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spaces.Space8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatTag(
                        icon = Icons.Rounded.Schedule,
                        text = preview.duration,
                        containerColor = Color(0xFFF3F4F6),
                        contentColor = Color(0xFF4B5563)
                    )

                    StatTag(
                        icon = Icons.Rounded.EmojiEvents,
                        text = stringResource(R.string.label_xp_reward, preview.xpReward),
                        containerColor = Color(0xFFFEFCE8),
                        contentColor = Color(0xFFCA8A04)
                    )
                }

                Spacer(modifier = Modifier.height(Spaces.Space32))

                Text(
                    text = preview.content,
                    color = AppColors.GrayText,
                    fontSize = AppTextSizes.TextSizeMain,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(Spaces.Space32))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Spaces.Space16),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Star,
                            null,
                            tint = AppColors.BluePrimary,
                            modifier = Modifier.size(AppIcons.IconSizeSmall)
                        )
                        Spacer(Modifier.width(Spaces.Space8))
                        Text(
                            text = stringResource(R.string.label_what_will_you_learn),
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TitleColor,
                            fontSize = AppTextSizes.TextSizeMain
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEFF6FF), RoundedCornerShape(Spaces.Space20))
                            .border(Spaces.Space1, Color(0xFFDBEAFE), RoundedCornerShape(Spaces.Space20))
                            .padding(Spaces.Space20)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spaces.Space14)) {
                            preview.outcomes.forEach { outcome ->
                                ObjectiveItem(outcome)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spaces.Space16))
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent,
        ) {
            Box(modifier = Modifier.padding(Spaces.Space24)) {
                if (isCompleted) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spaces.Space12)
                    ) {
                        Button(
                            onClick = { onReviewClick(preview.id) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(20.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFF1F2),
                                contentColor = Color(0xFFE11D48)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = stringResource(R.string.label_review),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = AppTextSizes.TextSizeMain
                            )
                        }

                        Button(
                            onClick = { onRestartClick(preview.id) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(20.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEFF6FF),
                                contentColor = Color(0xFF2563EB)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Replay,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = stringResource(R.string.label_restart),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = AppTextSizes.TextSizeMain
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            onStartClick(preview.id)
                                  },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Spaces.Space56),
                        shape = RoundedCornerShape(Spaces.Space16),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                    ) {
                        Icon(Icons.Rounded.PlayCircle, null, tint = Color.White)
                        Spacer(Modifier.width(Spaces.Space8))
                        Text(
                            text = stringResource(R.string.action_start_learning),
                            color = Color.White,
                            fontWeight = FontWeight.W700,
                            fontSize = AppTextSizes.TextSizeTitle
                        )
                    }
                }
            }
        }
    }
}

