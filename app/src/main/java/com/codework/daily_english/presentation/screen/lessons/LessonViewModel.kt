package com.codework.daily_english.presentation.screen.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.WordItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LessonViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<LessonData>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    // Kênh để bắn sự kiện điều hướng (One-time event) sang màn hình chi tiết
    // Bạn nên dùng SharedFlow hoặc Channel cho việc navigate
    // private val _navigationChannel = Channel<LessonNavigationRoute>() ...

    init {
        loadLessonData()
    }

    fun onEvent(event: LessonEvent) {
        when(event) {
            is LessonEvent.OnStepClick -> {
                // Logic: Kiểm tra xem bước đó có bị khóa không
                val currentData = (_uiState.value as? UiState.Success)?.data ?: return
                val clickedStep = currentData.steps.find { it.id == event.stepId }

                if (clickedStep != null && clickedStep.status != StepStatus.LOCKED) {
                    println("Navigate to step: ${clickedStep.type}")
                    // TODO: Bắn event điều hướng sang màn hình tương ứng
                    // sendNavigationEvent(clickedStep.type)
                }
            }
            is LessonEvent.OnRetry -> loadLessonData()
            is LessonEvent.OnBackClick -> { /* Xử lý back nếu cần custom */ }
            is LessonEvent.OnStartPractice -> TODO()

        }
    }

    private fun loadLessonData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(500) // Giả lập network

            // --- MOCK DATA CHO UI MỚI ---
            val mockSteps = listOf(
                LessonStep(
                    id = "step_1",
                    title = "Key Vocabulary",
                    subtitle = "Learn 5 essential words",
                    type = LessonStepType.VOCABULARY,
                    status = StepStatus.COMPLETED // Đã xong -> Màu xanh
                ),
                LessonStep(
                    id = "step_2",
                    title = "Dialogue: Ordering",
                    subtitle = "Listen to a conversation",
                    type = LessonStepType.DIALOGUE,
                    status = StepStatus.UNLOCKED // Đang học -> Màu xanh dương/nổi
                ),
                LessonStep(
                    id = "step_3",
                    title = "Roleplay with AI",
                    subtitle = "Practice speaking order",
                    type = LessonStepType.SPEAKING_AI,
                    status = StepStatus.LOCKED // Chưa học -> Màu xám
                ),
                LessonStep(
                    id = "step_4",
                    title = "Final Quiz",
                    subtitle = "Test your knowledge",
                    type = LessonStepType.QUIZ,
                    status = StepStatus.LOCKED
                )
            )

            val mockData = LessonData(
                lessonId = "lesson_001",
                title = "Checking in at the Airport",
                description = "Learn how to communicate effectively at the check-in counter.",
                imageUrl = "https://example.com/airport.jpg",
                totalProgress = 0.25f, // 1 trên 4 bước hoàn thành
                steps = mutableListOf(),
                level = "A1 - B1"
            )

            _uiState.value = UiState.Success(mockData)
        }
    }
}