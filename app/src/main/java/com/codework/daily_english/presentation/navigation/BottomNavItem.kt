package com.codework.daily_english.presentation.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GTranslate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.GTranslate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Preview
import androidx.compose.material.icons.outlined.Route
import androidx.compose.ui.graphics.vector.ImageVector
import com.codework.daily_english.R

sealed class BottomNavItem (
    val route: Route,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val title: Int
){
    object Home : BottomNavItem(
        route = Route.HomeScreen,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        title = R.string.nav_home
    )

    object Courses: BottomNavItem(
        route = Route.CourseScreen,
        icon = Icons.Outlined.GTranslate,
        selectedIcon = Icons.Filled.GTranslate,
        title = R.string.nav_course
    )

    object Practice: BottomNavItem(
        route = Route.PracticeScreen,
        icon = Icons.Outlined.Preview,
        selectedIcon = Icons.Filled.Preview,
        title = R.string.nav_practice,
    )

    object More: BottomNavItem(
        route = Route.MoreScreen,
        icon = Icons.Outlined.Route,
        selectedIcon = Icons.Filled.Route,
        title = R.string.nav_more,
    )
}