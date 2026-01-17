package com.codework.daily_english.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codework.daily_english.presentation.screen.courses.CourseScreen
import com.codework.daily_english.presentation.screen.home.HomeScreen
import com.codework.daily_english.presentation.screen.more.MoreScreen
import com.codework.daily_english.presentation.screen.practice.PracticeScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.HomeScreen
    ) {
        composable<Route.HomeScreen> {
            HomeScreen(
//                onNavigateToMeasure = { navController.navigate(Route.MeasureScreen) },
//                onNavigateToMap = { navController.navigate(Route.MapScreen) },
//                onNavigateToHistory = { navController.navigate(Route.HistoryScreen) }
            )
        }
        composable<Route.CourseScreen> {
            CourseScreen()
        }
        composable<Route.PracticeScreen> {
             PracticeScreen()
        }
        composable<Route.MoreScreen> {
            MoreScreen()
        }
    }
}