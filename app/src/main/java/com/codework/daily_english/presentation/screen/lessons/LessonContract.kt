package com.codework.daily_english.presentation.screen.lessons

enum class LessonStepType {
    VOCABULARY,
    LISTENING,
    SENTENCE_BUILDER,
    SPEAKING_AI,
    QUIZ
}

enum class StepStatus { LOCKED, UNLOCKED, COMPLETED }

data class LessonStep(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: LessonStepType,
    val status: StepStatus
)

// 1. STATE (Đổi tên LessonData -> LessonState)
data class LessonState(
    val lessonId: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val totalProgress: Float = 0.0f,
    val steps: List<LessonStep> = emptyList(),
    val level: String = ""
)

// 2. EVENT
sealed interface LessonEvent {
    data class OnStepClick(val stepId: String) : LessonEvent
    data object OnRetry : LessonEvent
    data object OnStartPractice : LessonEvent
}

// 3. EFFECT
sealed interface LessonEffect {
    data object NavigateToVocabulary : LessonEffect
    data object NavigateToListening : LessonEffect
    data object NavigateToSentenceBuilder : LessonEffect
    data object NavigateToSpeaking : LessonEffect
    data object NavigateToQuiz : LessonEffect
    data class ShowToast(val message: String) : LessonEffect
}