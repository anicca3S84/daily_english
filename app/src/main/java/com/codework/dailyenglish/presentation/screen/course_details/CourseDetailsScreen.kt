package com.codework.dailyenglish.presentation.screen.course_details

import android.R.attr.bottom
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.R
import androidx.navigation.NavController
import com.codework.dailyenglish.core.navigation.Route
import com.codework.dailyenglish.presentation.components.AppProgressBar
import com.codework.dailyenglish.presentation.components.LoadingType
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.course_details.components.CourseHeroSection
import com.codework.dailyenglish.presentation.screen.course_details.components.CourseStatsSection
import com.codework.dailyenglish.presentation.screen.course_details.components.LessonAccordionItem
import com.codework.dailyenglish.presentation.screen.course_details.components.ModuleAccordionItem
import com.codework.dailyenglish.presentation.screen.course_details.model.LessonStatus
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailEvent
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailState
import com.codework.dailyenglish.presentation.screen.lessons.preview.LessonPreviewPopup
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.PlatformTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    state: CourseDetailState,
    onEvent: (CourseDetailEvent) -> Unit,
    onBackClick: () -> Unit,
    navController: NavController
) {
    var showPreview by remember { mutableStateOf(false) }
    var selectedLessonId by remember { mutableStateOf("") }
    var expandedModuleIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = AppColors.AppBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.label_course_details),
                        fontWeight = FontWeight.Bold,
                        fontSize = AppTextSizes.TextSizeTitle,
                        textAlign = TextAlign.Center,
                        color = AppColors.TextMain
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppColors.TextMain
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = AppColors.TextMain
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            StateWrapper(
                uiState = state.modulesState,
                modifier = Modifier.fillMaxSize(),
                enablePullToRefresh = true,
                loadingType = LoadingType.CIRCULAR,
                onRetry = { onEvent(CourseDetailEvent.OnRefresh) }
            ) { modules ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Spaces.Space20,
                        end = Spaces.Space20,
                        top = Spaces.Space16,
                        bottom = Spaces.Space20
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spaces.Space30)
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spaces.Space16)
                        ) {
                            CourseHeroSection(state = state)
                            CourseStatsSection(state = state)
                        }
                    }

                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spaces.Space8)
                        ) {
                            Text(
                                text = stringResource(R.string.label_course_introduction),
                                fontWeight = FontWeight.Bold,
                                fontSize = AppTextSizes.TextSizeTitle,
                                color = AppColors.TextMain,
                            )

                            Text(
                                text = state.introduction,
                                fontSize = AppTextSizes.TextSizeBody,
                                color = AppColors.TextSub,
                                lineHeight = AppTextSizes.TextSize24,
                                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                            )
                        }
                    }


                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(Spaces.Space12)) {
                            Spacer(modifier = Modifier.height(Spaces.Space2))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.label_course_progress),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = AppTextSizes.TextSizeTitle,
                                    color = AppColors.TextMain,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(Spaces.Space4)
                            ) {
                                AppProgressBar(
                                    progress = state.overallProgress,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "${stringResource(R.string.label_completed)} ${state.completedLessons}/${state.totalLessons}",
                                    color = AppColors.BlueMain,
                                    fontSize = AppTextSizes.TextSizeLabel,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(Spaces.Space2))
                        }
                    }


                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(Spaces.Space12)) {
                            Text(
                                text = stringResource(R.string.label_course_content),
                                fontWeight = FontWeight.Bold,
                                fontSize = AppTextSizes.TextSizeTitle,
                                color = AppColors.TextMain
                            )

                            modules?.forEachIndexed { index, module ->
                                ModuleAccordionItem(
                                    title = module.title,
                                    description = module.description,
                                    lessonsCount = module.lessons.size,
                                    isExpanded = expandedModuleIndex == index,
                                    index = index,
                                    onToggle = {
                                        expandedModuleIndex = if (expandedModuleIndex == index) -1 else index
                                    }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(
                                            start = Spaces.Space16,
                                            end = Spaces.Space16,
                                            bottom = Spaces.Space16
                                        )
                                    ) {
                                        HorizontalDivider(
                                            color = AppColors.GrayBorder,
                                            modifier = Modifier.padding(bottom = Spaces.Space8)
                                        )

                                        module.lessons.forEach { lesson ->
                                            LessonAccordionItem(
                                                lesson = lesson,
                                                onClick = {
                                                    if (lesson.status != LessonStatus.LOCKED) {
                                                        selectedLessonId = lesson.id
                                                        showPreview = true
                                                    } else {
                                                        onEvent(CourseDetailEvent.OnLessonClick(lesson.id, true))
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                val currentLesson = modules?.flatMap { it.lessons }?.find { it.id == selectedLessonId }
                val isCompleted = (currentLesson?.status == LessonStatus.COMPLETED)
                if (showPreview && currentLesson != null) {
                    LessonPreviewPopup(
                        lessonId = selectedLessonId,
                        visible = true,
                        isCompleted = isCompleted,
                        onDismiss = { showPreview = false },
                        onNavigateToLearn = { lessonId, isReview ->
                            showPreview = false
                            navController.navigate(
                                Route.LessonScreen(
                                    lessonId = lessonId,
                                    isCompleted = isCompleted,
                                    isReview = isReview
                                )
                            )
                        },
                        courseTheme = state.courseTheme
                    )
                }
            }
        }
    }
}