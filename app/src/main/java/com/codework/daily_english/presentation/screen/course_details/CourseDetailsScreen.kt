package com.codework.daily_english.presentation.screen.course_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.codework.daily_english.R
import com.codework.daily_english.presentation.components.CurvedHeaderWrapper
import com.codework.daily_english.presentation.components.GridBackground
import com.codework.daily_english.presentation.components.HeaderStatItem
import com.codework.daily_english.presentation.components.LoadingType
import com.codework.daily_english.presentation.components.StateWrapper
import com.codework.daily_english.presentation.screen.course_details.components.LessonTimelineItem
import com.codework.daily_english.presentation.screen.course_details.components.ModuleHeaderLabel
import com.codework.daily_english.presentation.screen.course_details.components.VerticalDividerCourse
import com.codework.daily_english.presentation.screen.course_details.model.LessonStatus
import com.codework.daily_english.presentation.screen.course_details.mvi.CourseDetailEvent
import com.codework.daily_english.presentation.screen.course_details.mvi.CourseDetailState
import com.codework.daily_english.presentation.theme.Spaces
import com.codework.daily_english.presentation.theme.TextSizes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseDetailsScreen(
    state: CourseDetailState,
    onEvent: (CourseDetailEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val snackBar = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBar) },
        containerColor = Color.Transparent
    ) { _ ->

        Box(modifier = Modifier.fillMaxSize()) {

            GridBackground()

            StateWrapper(
                uiState = state.modulesState,
                modifier = Modifier.fillMaxSize(),
                snackbarHostState = snackBar,
                enablePullToRefresh = true,
                loadingType = LoadingType.CIRCULAR,
                onRetry = { onEvent(CourseDetailEvent.OnRefresh) }
            ) { modules ->

                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    stickyHeader {
                        CurvedHeaderWrapper(
                            backgroundBrush = state.courseTheme.headerColor,
                            content = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = Spaces.Space8)
                                        .statusBarsPadding(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(
                                        onClick = onBackClick,
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.15f))
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = stringResource(R.string.label_back),
                                            tint = Color.White
                                        )
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(top = Spaces.Space8)
                                    ) {
                                        Text(
                                            text = state.courseTitle,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = TextSizes.TextSize20
                                        )

                                        Spacer(modifier = Modifier.height(Spaces.Space12))

                                        Text(
                                            text = stringResource(
                                                R.string.label_overall_progress,
                                                (state.overallProgress * 100).toInt()
                                            ),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.White.copy(alpha = 0.9f)
                                        )

                                        Spacer(modifier = Modifier.height(Spaces.Space14))

                                        LinearProgressIndicator(
                                            progress = { state.overallProgress },
                                            modifier = Modifier
                                                .width(Spaces.Space165)
                                                .height(Spaces.Space8)
                                                .clip(RoundedCornerShape(Spaces.Space50)),
                                            color = Color.White,
                                            trackColor = Color.White.copy(alpha = 0.3f),
                                            strokeCap = StrokeCap.Round
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(Spaces.Space20))
                            },
                            bottomCardContent = {
                                HeaderStatItem(
                                    value = state.totalLessons.toString(),
                                    label = stringResource(R.string.label_lessons),
                                    icon = Icons.Default.Book,
                                    iconColor = state.courseTheme.primaryColor
                                )
                                VerticalDividerCourse()
                                HeaderStatItem(
                                    value = state.completedLessons.toString(),
                                    label = stringResource(R.string.label_completed),
                                    icon = Icons.Default.CheckCircle,
                                    iconColor = state.courseTheme.primaryColor
                                )
                            }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(Spaces.Space6)) }

                    modules?.forEach { module ->

                        item {
                            ModuleHeaderLabel(
                                title = module.title,
                                description = module.description,
                                theme = state.courseTheme
                            )
                            Spacer(modifier = Modifier.height(Spaces.Space16))
                        }

                        itemsIndexed(module.lessons) { index, lesson ->
                            LessonTimelineItem(
                                lesson = lesson,
                                index = index,
                                isLastItem = index == module.lessons.lastIndex,
                                onClick = {
                                    val isLocked = lesson.status == LessonStatus.LOCKED
                                    onEvent(
                                        CourseDetailEvent.OnLessonClick(
                                            lesson.id,
                                            isLocked
                                        )
                                    )
                                },
                                theme = state.courseTheme
                            )
                        }

                        item { Spacer(modifier = Modifier.height(Spaces.Space16)) }
                    }

                    item { Spacer(modifier = Modifier.height(Spaces.Space32)) }
                }
            }
        }
    }
}