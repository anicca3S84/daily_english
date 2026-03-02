package com.codework.daily_english.presentation.screen.lessons.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.QuizType
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<QuizReviewData>() {

    // Lưu trữ toàn bộ danh sách câu hỏi
    private var reviewList: List<QuizReviewData> = emptyList()
    // Lưu vị trí câu hỏi hiện tại
    private var currentIndex = 0
    // Lưu loại bài tập hiện tại (Lấy từ Navigation)
    private var currentQuizType: QuizType = QuizType.VOCABULARY

    init {
        // Hứng biến quizType từ Route (Nhờ Jetpack Navigation Type-Safe)
        val args = savedStateHandle.toRoute<Route.QuizReviewScreen>()
        currentQuizType = args.quizType

        // Gọi hàm load data
        loadQuizData(currentQuizType)
    }

    fun onEvent(event: QuizReviewEvent) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        when (event) {
            is QuizReviewEvent.OnGoBack -> {
                // Xử lý khi bấm nút Back (Có thể emit event để quay lại màn trước)
            }

            is QuizReviewEvent.OnSelectOption -> {
                // Chỉ cho phép chọn 1 lần (nếu chưa chọn thì mới cập nhật)
                if (currentState.selectedOptionIndex == null) {
                    _uiState.value = UiState.Success(
                        currentState.copy(selectedOptionIndex = event.index)
                    )
                }
            }

            is QuizReviewEvent.OnNextQuestion -> {
                // Chuyển sang câu hỏi tiếp theo
                if (currentIndex < reviewList.size - 1) {
                    currentIndex++
                    updateUiWithCurrentQuestion()
                }
            }

            is QuizReviewEvent.OnNavigateToListeningScreen -> {
                navigateTo(Route.ListeningScreen)
            }

            is QuizReviewEvent.OnNavigateToGrammarScreen -> {
                navigateTo(Route.GrammarScreen)
            }
        }
    }

    private fun loadQuizData(type: QuizType) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(400) // Giả lập load data

            if (type == QuizType.VOCABULARY) {
                // DATA TỪ VỰNG (Hiển thị 2 cột)
                reviewList = listOf(
                    QuizReviewData(
                        id = "rev_v_001",
                        type = QuizType.VOCABULARY,
                        sentence = "They paid for his flights and hotel ________.",
                        translatedSentence = "Họ đã trả tiền vé máy bay và [nơi ở] khách sạn cho anh ấy.",
                        options = listOf("evaluate", "accommodation", "persistent", "obvious"),
                        correctAnswerIndex = 1,
                        progressCurrent = 1,
                        progressTotal = 3
                    ),
                    QuizReviewData(
                        id = "rev_v_002",
                        type = QuizType.VOCABULARY,
                        sentence = "We need to ________ the success of the campaign.",
                        translatedSentence = "Chúng ta cần [đánh giá] sự thành công của chiến dịch.",
                        options = listOf("persistent", "accommodation", "evaluate", "obvious"),
                        correctAnswerIndex = 2,
                        progressCurrent = 2,
                        progressTotal = 3
                    ),
                    QuizReviewData(
                        id = "rev_v_003",
                        type = QuizType.VOCABULARY,
                        sentence = "She has been ________ in her pursuit of the truth.",
                        translatedSentence = "Cô ấy đã rất [kiên trì] trong việc theo đuổi sự thật.",
                        options = listOf("persistent", "evaluate", "accommodation", "obvious"),
                        correctAnswerIndex = 0,
                        progressCurrent = 3,
                        progressTotal = 3
                    )
                )
            } else {
                // DATA LISTENING (Hiển thị 1 cột dọc)
                reviewList = listOf(
                    QuizReviewData(
                        id = "rev_l_001",
                        type = QuizType.LISTENING,
                        sentence = "What size of coffee did the customer order?",
                        translatedSentence = "Vị khách đã gọi ly cà phê cỡ nào?",
                        options = listOf("A small size", "A medium size with oat milk", "A large size"),
                        correctAnswerIndex = 1,
                        progressCurrent = 1,
                        progressTotal = 2
                    ),
                    QuizReviewData(
                        id = "rev_l_002",
                        type = QuizType.LISTENING,
                        sentence = "How much did the coffee cost?",
                        translatedSentence = "Ly cà phê có giá bao nhiêu?",
                        options = listOf("$3.50", "$4.50", "$5.00"),
                        correctAnswerIndex = 1,
                        progressCurrent = 2,
                        progressTotal = 2
                    )
                )
            }
            currentIndex = 0
            updateUiWithCurrentQuestion()
        }
    }

    private fun updateUiWithCurrentQuestion() {
        if (reviewList.isNotEmpty() && currentIndex < reviewList.size) {
            // Lấy câu hỏi tại vị trí currentIndex và ném ra cho UI vẽ
            _uiState.value = UiState.Success(reviewList[currentIndex])
        }
    }
}