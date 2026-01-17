package com.codework.daily_english.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object HomeScreen : Route

    @Serializable
    data object CourseScreen : Route

    @Serializable
    data object PracticeScreen : Route

    @Serializable
    data object MoreScreen : Route
}