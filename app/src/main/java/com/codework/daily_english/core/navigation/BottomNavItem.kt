package com.codework.daily_english.core.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.codework.daily_english.R
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Person

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
        icon = Icons.AutoMirrored.Outlined.LibraryBooks,
        selectedIcon = Icons.AutoMirrored.Filled.LibraryBooks,
        title = R.string.nav_course
    )

    object Practice: BottomNavItem(
        route = Route.PracticeScreen,
        icon = Icons.Outlined.Extension,
        selectedIcon = Icons.Filled.Extension,
        title = R.string.nav_practice,
    )


    object Grammar: BottomNavItem(
        route = Route.GrammarScreen,
        icon = Icons.Outlined.LibraryBooks,
        selectedIcon = Icons.Filled.LibraryBooks,
        title = R.string.nav_grammar,
    )

    object More: BottomNavItem(
        route = Route.MoreScreen,
        icon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person,
        title = R.string.nav_more,
    )
}