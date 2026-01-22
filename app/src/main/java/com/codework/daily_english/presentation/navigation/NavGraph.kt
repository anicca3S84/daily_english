package com.codework.daily_english.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.codework.daily_english.presentation.screen.ai.AiViewModel
import com.codework.daily_english.presentation.screen.courses.CourseScreen
import com.codework.daily_english.presentation.screen.courses.CourseViewModel
import com.codework.daily_english.presentation.screen.dictionary.DictionaryScreen
import com.codework.daily_english.presentation.screen.dictionary.DictionaryViewModel
import com.codework.daily_english.presentation.screen.home.HomeScreen
import com.codework.daily_english.presentation.screen.home.HomeViewModel
import com.codework.daily_english.presentation.screen.lessons.LessonEvent
import com.codework.daily_english.presentation.screen.lessons.LessonScreen
import com.codework.daily_english.presentation.screen.lessons.LessonViewModel
import com.codework.daily_english.presentation.screen.more.MoreScreen
import com.codework.daily_english.presentation.screen.more.MoreViewModel
import com.codework.daily_english.presentation.screen.practice.PracticeScreen
import com.codework.daily_english.presentation.screen.practice.PracticeViewModel
import com.codework.daily_english.presentation.screen.translate.TranslateScreen
import com.codework.daily_english.presentation.screen.translate.TranslateViewModel

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
            LaunchedEffect(viewModel.navigationChannel) {
                viewModel.navigationChannel.collect { route ->
                    navController.navigate(route)
                }
            }

            HomeScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
        composable<Route.CourseScreen> {
            val viewModel = hiltViewModel<CourseViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(viewModel.navigationChannel) {
                viewModel.navigationChannel.collect { route ->
                    navController.navigate(route)
                }
            }
            CourseScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }

        composable<Route.LessonScreen> {
            val viewModel = hiltViewModel<LessonViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel.navigationChannel) {
                viewModel.navigationChannel.collect { route ->
                    navController.navigate(route)
                }
            }
            LessonScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<Route.PracticeScreen> {
            val viewModel = hiltViewModel<PracticeViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel.navigationChannel) {
                viewModel.navigationChannel.collect { route ->
                    navController.navigate(route)
                }
            }
            PracticeScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }
        composable<Route.MoreScreen> {
            val viewModel = hiltViewModel<MoreViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel.navigationChannel) {
                viewModel.navigationChannel.collect { route ->
                    navController.navigate(route)
                }
            }
            MoreScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }

        composable<Route.AIScreen> {
            val viewModel = hiltViewModel<AiViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(viewModel.navigationChannel) {
                viewModel.navigationChannel.collect { route ->
                    navController.navigate(route)
                }
            }
            AiScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.DictionaryScreen> {
            val viewModel = hiltViewModel<DictionaryViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(viewModel.navigationChannel) {
                viewModel.navigationChannel.collect { route ->
                    navController.navigate(route)
                }
            }
            DictionaryScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<Route.TranslateScreen> {
            val viewModel = hiltViewModel<TranslateViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(viewModel.navigationChannel) {
                viewModel.navigationChannel.collect { route ->
                    navController.navigate(route)
                }
            }
            TranslateScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}