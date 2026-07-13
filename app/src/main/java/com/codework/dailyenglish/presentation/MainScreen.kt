package com.codework.dailyenglish.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codework.dailyenglish.core.navigation.AppBottomNavigation
import com.codework.dailyenglish.core.navigation.NavGraph
import com.codework.dailyenglish.core.navigation.Route


import com.codework.dailyenglish.core.utils.AppTimeTracker

@Composable
fun MainScreen(
    startDestination: Any,
    onSaveSessionTime: (Int) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isUserLoggedIn = currentDestination != null && !currentDestination.hasRoute<Route.LoginScreen>()

    if (isUserLoggedIn) {
        AppTimeTracker(onSaveSessionTime = onSaveSessionTime)
    }

    val showBottomBar = currentDestination?.hasRoute<Route.HomeScreen>() == true ||
            currentDestination?.hasRoute<Route.CourseScreen>() == true ||
            currentDestination?.hasRoute<Route.PracticeScreen>() == true ||
            currentDestination?.hasRoute<Route.MoreScreen>() == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigation(navController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (showBottomBar) {
                        Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    } else {
                        Modifier
                    }
                )
        ) {
            NavGraph(
                navController = navController,
                startDestination = startDestination
            )
        }
    }
}