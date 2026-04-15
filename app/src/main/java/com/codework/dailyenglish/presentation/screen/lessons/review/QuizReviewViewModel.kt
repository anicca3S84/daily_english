package com.codework.dailyenglish.presentation.screen.lessons.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.presentation.screen.lessons.review.model.QuizType
import com.codework.dailyenglish.core.navigation.Route
import com.codework.dailyenglish.presentation.screen.lessons.review.mvi.QuizReviewEffect
import com.codework.dailyenglish.presentation.screen.lessons.review.mvi.QuizReviewEvent
import com.codework.dailyenglish.presentation.screen.lessons.review.mvi.QuizReviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<QuizReviewState, QuizReviewEvent, QuizReviewEffect>(
    initialState = QuizReviewState()
) {

    // Dùng QuizReviewState thay vì QuizReviewData
    private var reviewList: List<QuizReviewState> = emptyList()
    private var currentIndex = 0
    private var currentQuizType: QuizType = QuizType.VOCABULARY

    init {
        val args = savedStateHandle.toRoute<Route.QuizReviewScreen>()
        currentQuizType = args.quizType
        loadQuizData(currentQuizType)
    }

    override fun onEvent(event: QuizReviewEvent) {
//        val currentState = (_uiState.value as? BaseState.Success)?.data ?: return

        when (event) {
            is QuizReviewEvent.OnGoBack -> sendEffect(QuizReviewEffect.NavigateBack)

            is QuizReviewEvent.OnSelectOption -> {
//                if (currentState.selectedOptionIndex == null) {
//                    val updatedState = currentState.copy(selectedOptionIndex = event.index)
//                    // Cập nhật lại list ở background để khi bấm Next vẫn giữ nguyên đáp án nếu lùi lại
//                    val mutableList = reviewList.toMutableList()
//                    mutableList[currentIndex] = updatedState
//                    reviewList = mutableList
//
//                    _uiState.update { BaseState.Success(updatedState) }
//                }
            }

            is QuizReviewEvent.OnNextQuestion -> {
                if (currentIndex < reviewList.size - 1) {
                    currentIndex++
                    updateUiWithCurrentQuestion()
                }
            }

            is QuizReviewEvent.OnNavigateToListeningScreen -> sendEffect(QuizReviewEffect.NavigateToListening)
            is QuizReviewEvent.OnNavigateToGrammarScreen -> sendEffect(QuizReviewEffect.NavigateToGrammar)
        }
    }

    private fun loadQuizData(type: QuizType) {
        viewModelScope.launch {
//            _state.update { BaseState.Loading }
            delay(400) // Giả lập mạng

            if (type == QuizType.VOCABULARY) {
                reviewList = listOf(
                    QuizReviewState( // Đổi thành QuizReviewState
                        id = "rev_v_001",
                        type = QuizType.VOCABULARY,
                        sentence = "They paid for his flights and hotel ________.",
                        translatedSentence = "Họ đã trả tiền vé máy bay và [nơi ở] khách sạn cho anh ấy.",
                        options = listOf("evaluate", "accommodation", "persistent", "obvious"),
                        correctAnswerIndex = 1,
                        progressCurrent = 1,
                        progressTotal = 3
                    ),
                    QuizReviewState(
                        id = "rev_v_002",
                        type = QuizType.VOCABULARY,
                        sentence = "We need to ________ the success of the campaign.",
                        translatedSentence = "Chúng ta cần [đánh giá] sự thành công của chiến dịch.",
                        options = listOf("persistent", "accommodation", "evaluate", "obvious"),
                        correctAnswerIndex = 2,
                        progressCurrent = 2,
                        progressTotal = 3
                    ),
                    QuizReviewState(
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
                reviewList = listOf(
                    QuizReviewState(
                        id = "rev_l_001",
                        type = QuizType.LISTENING,
                        sentence = "What size of coffee did the customer order?",
                        translatedSentence = "Vị khách đã gọi ly cà phê cỡ nào?",
                        options = listOf("A small size", "A medium size with oat milk", "A large size"),
                        correctAnswerIndex = 1,
                        progressCurrent = 1,
                        progressTotal = 2
                    ),
                    QuizReviewState(
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
//            _uiState.update { BaseState.Success(reviewList[currentIndex]) }
        }
    }
}