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
import com.codework.daily_english.presentation.navigation.AppBottomNavigation
import com.codework.daily_english.presentation.navigation.NavGraph
import com.codework.daily_english.presentation.navigation.Route

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // 1. Lắng nghe sự thay đổi màn hình (BackStack)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 2. Logic kiểm tra: Đang ở màn hình nào thì hiện BottomBar?
    // Chỉ hiện khi currentDestination khớp với một trong các Route chính
    val showBottomBar = currentDestination?.hasRoute<Route.HomeScreen>() == true ||
            currentDestination?.hasRoute<Route.CourseScreen>() == true ||
            currentDestination?.hasRoute<Route.PracticeScreen>() == true ||
            currentDestination?.hasRoute<Route.MoreScreen>() == true

    Scaffold(
        bottomBar = {
            // 3. Chỉ render BottomBar nếu biến showBottomBar là true
            if (showBottomBar) {
                AppBottomNavigation(navController)
            }
        }
    ) { innerPadding ->
        // 4. innerPadding sẽ tự động thay đổi:
        // - Nếu có BottomBar: bottom padding = chiều cao BottomBar
        // - Nếu KHÔNG có BottomBar: bottom padding = 0 (Full màn hình)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            NavGraph(navController = navController)
        }
    }
}