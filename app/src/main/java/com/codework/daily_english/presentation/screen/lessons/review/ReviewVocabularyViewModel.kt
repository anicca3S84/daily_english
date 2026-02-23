package com.codework.daily_english.presentation.screen.lessons.review
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.navigation.Route
import com.codework.daily_english.presentation.screen.lessons.vocabulary.VocabularyEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewVocabularyViewModel @Inject constructor() : BaseViewModel<ReviewVocabularyData>() {

    private var reviewList = listOf<ReviewVocabularyData>()
    private var currentIndex = 0

    init {
        loadReviewList()
    }

    fun onEvent(event: ReviewVocabularyEvent) {
        when (event) {
            is ReviewVocabularyEvent.OnSelectOption -> {
                val currentData = reviewList.getOrNull(currentIndex) ?: return
                if (currentData.selectedOptionIndex == null) {
                    val updatedData = currentData.copy(
                        selectedOptionIndex = event.index,
                        isAnswerRevealed = true
                    )
                    reviewList = reviewList.toMutableList().apply { set(currentIndex, updatedData) }
                    _uiState.value = UiState.Success(updatedData)
                }
            }

            is ReviewVocabularyEvent.OnNextQuestion -> {
                if (currentIndex < reviewList.size - 1) {
                    currentIndex++
                    updateUiWithCurrentQuestion()
                } else {
                    // Chuyển sang màn Ngữ pháp hoặc Màn hoàn thành
                    // viewModelScope.launch { _navigationChannel.send(Route.GrammarScreen) }
                }
            }

            is ReviewVocabularyEvent.OnGoBack -> {
                if (currentIndex > 0) {
                    currentIndex--
                    updateUiWithCurrentQuestion()
                } else {
                    viewModelScope.launch { _navigationChannel.send(Route.LessonScreen) }
                }
            }

            is ReviewVocabularyEvent.OnNavigateToGrammarScreen -> {
                navigateTo(Route.GrammarScreen)
            }
        }
    }

    private fun loadReviewList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(400) // Giả lập load data

            // Mock Data lấy từ chính 3 từ vựng bạn vừa học
            reviewList = listOf(
                ReviewVocabularyData(
                    id = "rev_001",
                    sentence = "They paid for his flights and hotel ________.",
                    translatedSentence = "Họ đã trả tiền vé máy bay và [nơi ở] khách sạn cho anh ấy.",
                    options = listOf("evaluate", "accommodation", "persistent", "obvious"),
                    correctAnswerIndex = 1,
                    progressCurrent = 1,
                    progressTotal = 3
                ),
                ReviewVocabularyData(
                    id = "rev_002",
                    sentence = "We need to ________ the success of the campaign.",
                    translatedSentence = "Chúng ta cần [đánh giá] sự thành công của chiến dịch.",
                    options = listOf("persistent", "accommodation", "evaluate", "obvious"),
                    correctAnswerIndex = 2,
                    progressCurrent = 2,
                    progressTotal = 3
                ),
                ReviewVocabularyData(
                    id = "rev_003",
                    sentence = "She has been ________ in her pursuit of the truth.",
                    translatedSentence = "Cô ấy đã rất [kiên trì] trong việc theo đuổi sự thật.",
                    options = listOf("persistent", "evaluate", "accommodation", "obvious"),
                    correctAnswerIndex = 0,
                    progressCurrent = 3,
                    progressTotal = 3
                )
            )
            currentIndex = 0
            updateUiWithCurrentQuestion()
        }
    }

    private fun updateUiWithCurrentQuestion() {
        reviewList.getOrNull(currentIndex)?.let {
            _uiState.value = UiState.Success(it)
        }
    }
}