package com.codework.dailyenglish.presentation.screen.lessons.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun LessonPreviewSheet(
    state: LessonPreviewState,
    theme: CourseTheme,
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
                    onStartClick = { id ->
                        onEvent(LessonPreviewEvent.OnStartLearningClick(id))
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
    onStartClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = Spaces.Space24),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = Spaces.Space8, bottom = Spaces.Space24)
                        .size(Spaces.Space80 + Spaces.Space16)
                        .background(Color(0xFFDBEAFE), RoundedCornerShape(AppRadius.RadiusExtraLarge)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👋",
                        fontSize = AppTextSizes.TextSize48
                    )
                }

                Text(
                    text = preview.title,
                    color = Color(0xFF1F2937),
                    fontWeight = FontWeight.Black,
                    fontSize = AppTextSizes.TextSize26,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Spaces.Space16))

                Text(
                    text = preview.subtitle,
                    color = Color(0xFF4B5563),
                    textAlign = TextAlign.Center,
                    fontSize = AppTextSizes.TextSizeTitle,
                    lineHeight = Spaces.Space24.value.sp,
                    modifier = Modifier.padding(horizontal = Spaces.Space16)
                )

                Row(
                    modifier = Modifier.padding(top = Spaces.Space16, bottom = Spaces.Space24),
                    horizontalArrangement = Arrangement.spacedBy(Spaces.Space8)
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFF3F4F6), RoundedCornerShape(AppRadius.RadiusMedium))
                            .padding(horizontal = Spaces.Space12, vertical = Spaces.Space6),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(AppIcons.IconSizeExtraSmall),
                            tint = Color(0xFF4B5563)
                        )
                        Spacer(modifier = Modifier.width(Spaces.Space6))
                        Text(
                            text = preview.duration,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4B5563)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .background(Color(0xFFFEFCE8), RoundedCornerShape(AppRadius.RadiusMedium))
                            .padding(horizontal = Spaces.Space12, vertical = Spaces.Space6),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.EmojiEvents,
                            contentDescription = null,
                            modifier = Modifier.size(AppIcons.IconSizeExtraSmall),
                            tint = Color(0xFFEAB308)
                        )
                        Spacer(modifier = Modifier.width(Spaces.Space6))
                        Text(
                            text = stringResource(R.string.label_xp_reward, preview.xpReward),                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCA8A04)
                        )
                    }
                }

                Text(
                    text = preview.content,
                    color = AppColors.GrayText,
                    fontSize = AppTextSizes.TextSizeMain,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .padding(top = Spaces.Space16, bottom = Spaces.Space100)
                        .fillMaxWidth()
                        .background(Color(0xFFEFF6FF), RoundedCornerShape(Spaces.Space20))
                        .border(Spaces.Space1, Color(0xFFDBEAFE), RoundedCornerShape(Spaces.Space20))
                        .padding(Spaces.Space20)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = Spaces.Space8)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                tint = AppColors.BluePrimary,
                                modifier = Modifier.size(AppIcons.IconSizeSmall)
                            )
                            Spacer(modifier = Modifier.width(Spaces.Space8))
                            Text(
                                text = stringResource(R.string.label_what_will_you_learn),
                                fontWeight = FontWeight.Bold,
                                color = AppColors.GrayText,
                                fontSize = AppTextSizes.TextSizeMain
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(Spaces.Space14)) {
                            for (i in 0 until preview.outcomes.size){
                                ObjectiveItem(preview.outcomes[i])
                            }
                        }



                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spaces.Space24)
                .align(Alignment.BottomCenter),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spaces.Space64)
                    .clip(RoundedCornerShape(Spaces.Space16))
                    .clickable { onStartClick(preview.id) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = Spaces.Space6)
                        .background(Color(0xFF3B82F6), RoundedCornerShape(Spaces.Space16)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(AppIcons.IconSizeMedium)
                        )
                        Spacer(modifier = Modifier.width(Spaces.Space8))
                        Text(
                            text = stringResource(R.string.action_start_learning),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = AppTextSizes.TextSizeTitle,
                            letterSpacing = Spaces.Space1.value.sp
                        )
                    }
                }
            }
        }
    }
}