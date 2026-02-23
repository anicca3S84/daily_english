package com.codework.daily_english.presentation.screen.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class LessonViewModel @Inject constructor() : BaseViewModel<LessonData>() {

    init {
        loadLessonData()
    }

    fun onEvent(event: LessonEvent) {
        when(event) {
            is LessonEvent.OnStepClick -> {
                // Logic xử lý khi click vào bài học dựa trên stepId
                when (event.stepId) {
                    "1" -> {
                        // Nếu là bước 1 (Vocabulary) -> Chuyển sang VocabularyScreen
                        println("Navigating to Vocabulary")
                        navigateTo(Route.VocabularyScreen)
                    }
//                    "2" -> {
//                        // navigateTo(Route.ListeningScreen)
//                        println("Clicked Step 2: Listening")
//                    }
                    // Xử lý các step khác...
                    //fake
                    else ->    navigateTo(Route.VocabularyScreen)
                }
            }
            is LessonEvent.OnRetry -> loadLessonData()
            is LessonEvent.OnStartPractice -> {
                // Logic nút "Start Lesson" ở dưới đáy
                // Ví dụ: Tự động tìm bài chưa học đầu tiên và nhảy vào
                val currentData = (_uiState.value as? UiState.Success)?.data
                val nextStep = currentData?.steps?.firstOrNull { it.status != StepStatus.COMPLETED }

                if (nextStep?.id == "1") {
                    navigateTo(Route.VocabularyScreen)
                } else {
                    println("Start learning first unlocked lesson: ${nextStep?.title}")
                }
            }
        }
    }

    private fun loadLessonData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(500)

            val mockSteps = listOf(
                // BƯỚC 1: VOCABULARY
                LessonStep(
                    id = "1",
                    title = "Key Vocabulary",
                    subtitle = "Learn 5 new words",
                    type = LessonStepType.VOCABULARY,
                    status = StepStatus.UNLOCKED // Đổi thành UNLOCKED để test click được ngay
                ),

                // BƯỚC 2: LISTENING
                LessonStep(
                    id = "2",
                    title = "Dialogue: Ordering",
                    subtitle = "Fill in the blanks",
                    type = LessonStepType.LISTENING,
                    status = StepStatus.LOCKED
                ),

                // BƯỚC 3: SENTENCE BUILDER
                LessonStep(
                    id = "3",
                    title = "Grammar Focus",
                    subtitle = "Arrange words correctly",
                    type = LessonStepType.SENTENCE_BUILDER,
                    status = StepStatus.LOCKED
                ),

                // BƯỚC 4: SPEAKING AI
                LessonStep(
                    id = "4",
                    title = "Roleplay Challenge",
                    subtitle = "Order coffee with AI",
                    type = LessonStepType.SPEAKING_AI,
                    status = StepStatus.LOCKED
                ),

                // BƯỚC 5: QUIZ
                LessonStep(
                    id = "5",
                    title = "Unit Test",
                    subtitle = "Prove your mastery +50XP",
                    type = LessonStepType.QUIZ,
                    status = StepStatus.LOCKED
                )
            )

            val mockData = LessonData(
                lessonId = "lesson_001",
                title = "At the Coffee Shop",
                description = "Learn how to order drinks and snacks confidently like a local.",
                imageUrl = "https://picsum.photos/300/200",
                totalProgress = 0.0f,
                steps = mockSteps,
                level = "A1 - Beginner"
            )

            _uiState.value = UiState.Success(mockData)
        }
    }
}