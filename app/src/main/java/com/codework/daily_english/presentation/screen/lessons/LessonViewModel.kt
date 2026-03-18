package com.codework.daily_english.presentation.screen.lessons

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor() : BaseViewModel<LessonState, LessonEvent, LessonEffect>() {

    init {
        loadLessonData()
    }

    override fun onEvent(event: LessonEvent) {
        when(event) {
            is LessonEvent.OnStepClick -> {
                when (event.stepId) {
                    "1" -> sendEffect(LessonEffect.NavigateToVocabulary)
                    "2" -> sendEffect(LessonEffect.NavigateToListening)
                    "3" -> sendEffect(LessonEffect.NavigateToSentenceBuilder)
                    "4" -> sendEffect(LessonEffect.NavigateToSpeaking)
                    "5" -> sendEffect(LessonEffect.NavigateToQuiz)
                    else -> sendEffect(LessonEffect.NavigateToVocabulary)
                }
            }
            is LessonEvent.OnRetry -> loadLessonData()

            is LessonEvent.OnStartPractice -> {
                val currentData = (_uiState.value as? UiState.Success)?.data
                val nextStep = currentData?.steps?.firstOrNull { it.status != StepStatus.COMPLETED }

                when (nextStep?.id) {
                    "1" -> sendEffect(LessonEffect.NavigateToVocabulary)
                    "2" -> sendEffect(LessonEffect.NavigateToListening)
                    "3" -> sendEffect(LessonEffect.NavigateToSentenceBuilder)
                    "4" -> sendEffect(LessonEffect.NavigateToSpeaking)
                    "5" -> sendEffect(LessonEffect.NavigateToQuiz)
                    else -> sendEffect(LessonEffect.ShowToast("You have completed all steps!"))
                }
            }
        }
    }

    private fun loadLessonData() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            delay(500)

            val mockSteps = listOf(
                LessonStep("1", "Key Vocabulary", "Learn 5 new words", LessonStepType.VOCABULARY, StepStatus.UNLOCKED),
                LessonStep("2", "Dialogue: Ordering", "Fill in the blanks", LessonStepType.LISTENING, StepStatus.LOCKED),
                LessonStep("3", "Grammar Focus", "Arrange words correctly", LessonStepType.SENTENCE_BUILDER, StepStatus.LOCKED),
                LessonStep("4", "Roleplay Challenge", "Order coffee with AI", LessonStepType.SPEAKING_AI, StepStatus.LOCKED),
                LessonStep("5", "Unit Test", "Prove your mastery +50XP", LessonStepType.QUIZ, StepStatus.LOCKED)
            )

            val mockData = LessonState( // Đổi thành LessonState
                lessonId = "lesson_001",
                title = "At the Coffee Shop",
                description = "Learn how to order drinks and snacks confidently like a local.",
                imageUrl = "https://picsum.photos/300/200",
                totalProgress = 0.0f,
                steps = mockSteps,
                level = "A1 - Beginner"
            )

            _uiState.update { UiState.Success(mockData) }
        }
    }
}