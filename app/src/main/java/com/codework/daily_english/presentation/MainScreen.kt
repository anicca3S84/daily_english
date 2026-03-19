package com.codework.daily_english.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // Import cái này
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codework.daily_english.core.navigation.AppBottomNavigation
import com.codework.daily_english.core.navigation.NavGraph
import com.codework.daily_english.core.navigation.Route

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.hasRoute<Route.HomeScreen>() == true ||
            currentDestination?.hasRoute<Route.CourseScreen>() == true ||
            currentDestination?.hasRoute<Route.PracticeScreen>() == true ||
            currentDestination?.hasRoute<Route.GrammarScreen>() == true ||
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
            NavGraph(navController = navController)
        }
    }
}