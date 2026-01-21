package com.codework.daily_english.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codework.daily_english.presentation.screen.ai.AiScreen
import com.codework.daily_english.presentation.screen.courses.CourseScreen
import com.codework.daily_english.presentation.screen.courses.CourseViewModel
import com.codework.daily_english.presentation.screen.dictionary.DictionaryScreen
import com.codework.daily_english.presentation.screen.home.HomeScreen
import com.codework.daily_english.presentation.screen.lessons.LessonEvent
import com.codework.daily_english.presentation.screen.lessons.LessonScreen
import com.codework.daily_english.presentation.screen.lessons.LessonViewModel
import com.codework.daily_english.presentation.screen.more.MoreScreen
import com.codework.daily_english.presentation.screen.practice.PracticeScreen
import com.codework.daily_english.presentation.screen.translate.TranslateScreen

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Route.HomeScreen,
        modifier = Modifier.padding(bottom = 0.dp)
    ) {
        composable<Route.HomeScreen> {
            HomeScreen(
                onNavigateToAI = { navController.navigate(Route.AIScreen) },
                onNavigateToDictionary = { navController.navigate(Route.DictionaryScreen) },
                onNavigateToTranslate = { navController.navigate(Route.TranslateScreen) }
            )
        }
        composable<Route.CourseScreen> {
            // 1. Khởi tạo ViewModel (Scoped cho màn hình này)
            val viewModel = hiltViewModel<CourseViewModel>()

            // 2. Lấy State
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            // 3. Render UI và Nối dây
            CourseScreen(
                state = state,
                onEvent = viewModel::onEvent, // Truyền hàm onEvent của VM vào UI
                onNavigateToLesson = { topicId ->
                    // 4. Xử lý điều hướng TẠI ĐÂY
                    // Bạn có thể truyền topicId sang màn Lesson nếu cần
                    // navController.navigate(Route.LessonScreen(topicId))
                    navController.navigate(Route.LessonScreen)
                }
            )
        }
        composable<Route.PracticeScreen> {
            PracticeScreen()
        }
        composable<Route.MoreScreen> {
            MoreScreen()
        }

        composable<Route.AIScreen> {
            AiScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.DictionaryScreen> {
            DictionaryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<Route.TranslateScreen> {
            TranslateScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

// ... bên trong NavHost ...

                composable<Route.LessonScreen> {
                    // 1. Khởi tạo/Lấy ViewModel tại đây.
                    // Nó sẽ tự động scoped theo màn hình LessonScreen này.
                    val viewModel = hiltViewModel<LessonViewModel>()
                    LessonScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onStepClick = { stepId ->
                            // 2. Gọi event thông qua biến viewModel vừa tạo
                            viewModel.onEvent(LessonEvent.OnStepClick(stepId))
                            // Logic điều hướng (nếu cần xử lý tại đây hoặc lắng nghe từ VM)
                            // Ví dụ: navController.navigate(Route.Vocabulary(stepId))
                        }
                    )
                }
    }
}