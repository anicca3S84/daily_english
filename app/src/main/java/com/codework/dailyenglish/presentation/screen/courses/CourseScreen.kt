package com.codework.dailyenglish.presentation.screen.courses

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.stringResource
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.components.CurvedHeaderWrapper
import com.codework.dailyenglish.presentation.components.HeaderStatItem
import com.codework.dailyenglish.presentation.components.GridBackground
import com.codework.dailyenglish.presentation.components.LoadingType
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.courses.components.CourseCard
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseEvent
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseState
import com.codework.dailyenglish.presentation.theme.BluePrimary
import com.codework.dailyenglish.presentation.theme.Colors
import com.codework.dailyenglish.presentation.theme.IndigoPurple
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.TextSizes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseScreen(
    state: CourseState,
    onEvent: (CourseEvent) -> Unit,
) {
    val snackBar = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBar) },
        containerColor = Color.Transparent
    ) { _ ->
    Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            GridBackground()
            StateWrapper(
                uiState = state.dashboardState,
                modifier = Modifier.fillMaxSize(),
                snackbarHostState = snackBar,
                loadingType = LoadingType.CIRCULAR,
                enablePullToRefresh = true,
                onRetry = { onEvent(CourseEvent.OnRefreshData) }
            ) { courseList ->

                val safeCourses = courseList ?: emptyList()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = Spaces.Space20),
                    verticalArrangement = Arrangement.spacedBy(Spaces.Space16),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    stickyHeader {
                        CurvedHeaderWrapper(
                            content = {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = Spaces.Space24)
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = stringResource(R.string.label_continue_learning),
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Spacer(modifier = Modifier.height(Spaces.Space8))
                                            Text(
                                                text = stringResource(R.string.msg_study_to_keep_streak),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.White.copy(alpha = 0.9f)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(Spaces.Space16))

                                        Box(
                                            contentAlignment = Alignment.TopCenter,
                                            modifier = Modifier.padding(bottom = Spaces.Space20)
                                        ) {
                                            val streakValue = state.streakValue.toString()
                                            val textFontSize = when (streakValue.length) {
                                                1, 2 -> TextSizes.TextSizeHeader
                                                3 -> TextSizes.TextSizeTitle
                                                else -> TextSizes.TextSizeBody
                                            }

                                            Surface(
                                                modifier = Modifier.size(Spaces.Space68),
                                                color = Color.White.copy(alpha = 0.15f),
                                                shape = CircleShape,
                                                border = BorderStroke(Spaces.Space1_5, Color.White.copy(alpha = 0.5f))
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text("🔥", fontSize = TextSizes.TextSizeTitle)
                                                    Text(
                                                        text = streakValue,
                                                        color = Color.White,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        fontSize = textFontSize
                                                    )
                                                }
                                            }

                                            Surface(
                                                modifier = Modifier
                                                    .align(Alignment.BottomCenter)
                                                    .offset(y = Spaces.Space24),
                                                shape = RoundedCornerShape(Spaces.Space50),
                                                color = Colors.ExpBgColor,
                                                shadowElevation = Spaces.Space4
                                            ) {
                                                Text(
                                                    text = "${state.expValue} " + stringResource(R.string.label_exp),
                                                    modifier = Modifier.padding(horizontal = Spaces.Space10, vertical = Spaces.Space4),
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = TextSizes.TextSizeLabel
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            bottomCardContent = {
                                HeaderStatItem(
                                    value = state.lessonsDone.toString(),
                                    label = stringResource(R.string.label_lessons_done),
                                    icon = Icons.Default.CheckCircle,
                                    iconColor = BluePrimary
                                )
                                Divider(
                                    modifier = Modifier.height(Spaces.Space40).width(Spaces.Space1),
                                    color = Color.LightGray.copy(alpha = 0.5f)
                                )
                                HeaderStatItem(
                                    value = state.wordsLearned.toString(),
                                    label = stringResource(R.string.word_learned),
                                    icon = Icons.Default.PieChart,
                                    iconColor = IndigoPurple
                                )
                            }
                        )
                    }

                    items(safeCourses.chunked(2)) { rowCourses ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spaces.Space16),
                            horizontalArrangement = Arrangement.spacedBy(Spaces.Space16)
                        ) {
                            for (course in rowCourses) {
                                CourseCard(
                                    course = course,
                                    modifier = Modifier.weight(1f),
                                    isLocked = course.isLocked,
                                    onClick = {
                                        onEvent(CourseEvent.OnCourseClick(course.id, course.code, course.isLocked))
                                    }
                                )
                            }
                            if (rowCourses.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}