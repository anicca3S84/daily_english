package com.codework.daily_english.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.Flow // THÊM IMPORT NÀY

import com.codework.daily_english.presentation.screen.ai.AiScreen
import com.codework.daily_english.presentation.screen.ai.AiViewModel
import com.codework.daily_english.presentation.screen.courses.CourseScreen
import com.codework.daily_english.presentation.screen.courses.CourseViewModel
import com.codework.daily_english.presentation.screen.dictionary.DictionaryScreen
import com.codework.daily_english.presentation.screen.dictionary.DictionaryViewModel
import com.codework.daily_english.presentation.screen.home.HomeScreen
import com.codework.daily_english.presentation.screen.home.HomeViewModel
import com.codework.daily_english.presentation.screen.lessons.LessonScreen
import com.codework.daily_english.presentation.screen.lessons.LessonViewModel
import com.codework.daily_english.presentation.screen.lessons.listening.ListeningScreen
import com.codework.daily_english.presentation.screen.lessons.listening.ListeningViewModel
import com.codework.daily_english.presentation.screen.lessons.review.QuizReviewScreen
import com.codework.daily_english.presentation.screen.lessons.review.QuizReviewViewModel
import com.codework.daily_english.presentation.screen.lessons.vocabulary.VocabularyScreen
import com.codework.daily_english.presentation.screen.lessons.vocabulary.VocabularyViewModel
import com.codework.daily_english.presentation.screen.more.MoreScreen
import com.codework.daily_english.presentation.screen.more.MoreViewModel
import com.codework.daily_english.presentation.screen.practice.PracticeScreen
import com.codework.daily_english.presentation.screen.practice.PracticeViewModel
import com.codework.daily_english.presentation.screen.translate.TranslateScreen
import com.codework.daily_english.presentation.screen.translate.TranslateViewModel

@Composable
fun ObserveNavigation(
    navigationChannel: Flow<Route>,
    navController: NavHostController
) {
    LaunchedEffect(navigationChannel) {
        navigationChannel.collect { route ->
            navController.navigate(route)
        }
    }
}

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
            val viewModel = hiltViewModel<HomeViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            HomeScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }

        composable<Route.CourseScreen> {
            val viewModel = hiltViewModel<CourseViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            CourseScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }

        composable<Route.LessonScreen> {
            val viewModel = hiltViewModel<LessonViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            LessonScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.PracticeScreen> {
            val viewModel = hiltViewModel<PracticeViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            PracticeScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }

        composable<Route.MoreScreen> {
            val viewModel = hiltViewModel<MoreViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            MoreScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }

        composable<Route.AIScreen> {
            val viewModel = hiltViewModel<AiViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            AiScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.DictionaryScreen> {
            val viewModel = hiltViewModel<DictionaryViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            DictionaryScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.TranslateScreen> {
            val viewModel = hiltViewModel<TranslateViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            TranslateScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }


        composable<Route.VocabularyScreen> {
            val viewModel = hiltViewModel<VocabularyViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            VocabularyScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.QuizReviewScreen> {
            val viewModel = hiltViewModel<QuizReviewViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            QuizReviewScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.ListeningScreen> {
            val viewModel = hiltViewModel<ListeningViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveNavigation(viewModel.navigationChannel, navController)

            ListeningScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}