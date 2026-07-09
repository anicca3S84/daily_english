package com.codework.dailyenglish.core.navigation

import com.codework.dailyenglish.presentation.screen.lessons.review.model.QuizType
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object  LoginScreen: Route

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
    data class LessonScreen(val lessonId: String, val isCompleted: Boolean,val isReview: Boolean = false) : Route

    @Serializable
    data object VocabularyScreen : Route

    @Serializable
    data class QuizReviewScreen(val quizType: QuizType) : Route

    @Serializable
    data object ListeningScreen : Route

    @Serializable
    data object SentenceBuilderScreen : Route

    @Serializable
    data object SpeakingScreen : Route

    @Serializable
    data class CourseDetailScreen(
        val courseId: String,
        val courseCode: String
    ) : Route

    @Serializable
    data class LessonResultScreen(val scorePercentage: Int, val starsEarned: Int, val lessonId: String)

    @Serializable
    data object VocabularyListScreen : Route

    @Serializable
    data class TopicWordsScreen(
        val topicId: String,
        val topicName: String
    ) : Route

    @Serializable
    data object FlashcardScreen : Route
    @Serializable
    data object SubscriptionScreen : Route

    @Serializable
    data object LeaderboardScreen : Route

    @Serializable
    data class WordDetailScreen(
        val wordId: String
    ) : Route

    @Serializable
    data class TopicScreen(
        val origin: String
    ) : Route

    @Serializable
    data class AiSpeakingScreen(
        val topicId: String,
        val topicName: String
    ) : Route
}