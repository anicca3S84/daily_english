package com.codework.dailyenglish.presentation.screen.courses

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.components.BannerAd
import com.codework.dailyenglish.presentation.components.CurvedHeaderWrapper
import com.codework.dailyenglish.presentation.components.HeaderStatItem
import com.codework.dailyenglish.presentation.components.LoadingType
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.courses.components.CourseCard
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseEvent
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseState
import com.codework.dailyenglish.presentation.screen.subscription.SubscriptionPopup
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CourseScreen(
    state: CourseState,
    onEvent: (CourseEvent) -> Unit,
) {

    Scaffold(
        containerColor = AppColors.AppBackground
    ) { _ ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            PullToRefreshBox(
                isRefreshing = state.dashboardState.isRefreshing,
                onRefresh = { onEvent(CourseEvent.OnRefreshData) },
                modifier = Modifier.fillMaxSize()
            ) {
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
                                                1, 2 -> AppTextSizes.TextSizeHeader
                                                3 -> AppTextSizes.TextSizeTitle
                                                else -> AppTextSizes.TextSizeBody
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
                                                    Image(
                                                        painter = painterResource(id = R.drawable.ic_streak),
                                                        contentDescription = "Streak Icon",
                                                        modifier = Modifier
                                                            .size(Spaces.Space24)
                                                    )

                                                    Spacer(modifier = Modifier.height(Spaces.Space2))

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
                                                color = AppColors.ExpBgColor,
                                                shadowElevation = Spaces.Space4
                                            ) {
                                                Text(
                                                    text = "${state.expValue} " + stringResource(R.string.label_exp),
                                                    modifier = Modifier.padding(horizontal = Spaces.Space10, vertical = Spaces.Space4),
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = AppTextSizes.TextSizeLabel
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
                                    iconColor = AppColors.BluePrimary
                                )
                                Divider(
                                    modifier = Modifier.height(Spaces.Space40).width(Spaces.Space1),
                                    color = Color.LightGray.copy(alpha = 0.5f)
                                )
                                HeaderStatItem(
                                    value = state.wordsLearned.toString(),
                                    label = stringResource(R.string.word_learned),
                                    icon = Icons.Default.PieChart,
                                    iconColor = AppColors.IndigoPurple
                                )
                            }
                        )
                    }

                    // 2. StateWrapper bọc cục bộ riêng khu vực danh sách khóa học
                    item {
                        StateWrapper(
                            uiState = state.dashboardState,
                            modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 250.dp),
                            loadingType = LoadingType.CIRCULAR,
                            enablePullToRefresh = false, // Vô hiệu hóa ở đây vì đã bọc PullToRefreshBox ở ngoài cùng
                            onRetry = { onEvent(CourseEvent.OnRefreshData) }
                        ) { courseList ->
                            val safeCourses = courseList ?: emptyList()

                            // Kiểm tra nếu xảy ra lỗi kết nối mạng và không có dữ liệu hiển thị:
                            if (state.dashboardState.error != null && safeCourses.isEmpty()) {
                                // Trả về Box rỗng cao 250.dp để nút "Try again" tự căn giữa gọn gàng dưới Header
                                Box(modifier = Modifier.fillMaxWidth().height(250.dp))
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(Spaces.Space16)
                                ) {
                                    safeCourses.chunked(2).forEach { rowCourses ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = Spaces.Space16),
                                            horizontalArrangement = Arrangement.spacedBy(Spaces.Space16)
                                        ) {
                                            for (course in rowCourses) {
                                                val isPremiumCourse = course.code.uppercase() in listOf("B2", "C1", "C2")

                                                CourseCard(
                                                    course = course,
                                                    isPremium = isPremiumCourse,
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

                    // 3. Banner quảng cáo luôn hiển thị dưới cùng của danh sách
                    if (!state.isPremium) {
                        item {
                            Spacer(modifier = Modifier.height(Spaces.Space16))
                            BannerAd(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Spaces.Space16)
                            )
                        }
                    }
                }
            }

            if (state.showSubscriptionPopup) {
                SubscriptionPopup(
                    visible = true,
                    onDismiss = { onEvent(CourseEvent.OnDismissSubscriptionPopup) }
                )
            }
        }
    }
}