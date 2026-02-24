package com.codework.daily_english.presentation.navigation

import com.codework.daily_english.presentation.model.QuizType
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

    @Serializable
    data object AIScreen : Route

    @Serializable
    data object DictionaryScreen : Route

    @Serializable
    data object TranslateScreen : Route

    @Serializable
    data object GrammarScreen : Route

    @Serializable
    data object LessonScreen : Route

    @Serializable
    data object VocabularyScreen : Route

    @Serializable
    data class QuizReviewScreen(val quizType: QuizType) : Route

    @Serializable
    data object ListeningScreen : Route
}