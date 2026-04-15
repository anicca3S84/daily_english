package com.codework.dailyenglish.presentation.screen.lessons.vocabulary
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.presentation.screen.lessons.vocabulary.mvi.VocabularyEffect
import com.codework.dailyenglish.presentation.screen.lessons.vocabulary.mvi.VocabularyEvent
import com.codework.dailyenglish.presentation.screen.lessons.vocabulary.mvi.VocabularyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyViewModel @Inject constructor() : BaseViewModel<VocabularyState, VocabularyEvent, VocabularyEffect>(
    initialState = VocabularyState()
) {

    private var vocabularyList = listOf<VocabularyState>() // Dùng VocabularyState
    private var currentIndex = 0

    init {
        loadVocabularyList()
    }

    override fun onEvent(event: VocabularyEvent) {
        when (event) {
            is VocabularyEvent.OnPlayAudio -> sendEffect(VocabularyEffect.PlayAudio(event.region))

            is VocabularyEvent.OnNextWord -> {
                if (currentIndex < vocabularyList.size - 1) {
                    currentIndex++
                    updateUiWithCurrentWord()
                }
            }

            is VocabularyEvent.OnGoBack -> {
                if (currentIndex > 0) {
                    currentIndex--
                    updateUiWithCurrentWord()
                } else {
                    // Trở về màn hình bài học trước đó
                    sendEffect(VocabularyEffect.NavigateBack)
                }
            }

            is VocabularyEvent.OnRevealHint -> {
                val currentWord = vocabularyList.getOrNull(currentIndex) ?: return
                if (!currentWord.isHintRevealed) {
                    val updatedWord = currentWord.copy(isHintRevealed = true)
                    vocabularyList = vocabularyList.toMutableList().apply {
                        set(currentIndex, updatedWord)
                    }
//                    _state.value = BaseState.Success(updatedWord)
                }
            }

            is VocabularyEvent.OnSelectOption -> {
                val currentWord = vocabularyList.getOrNull(currentIndex) ?: return
                if (currentWord.selectedOptionIndex == null) {
                    val updatedWord = currentWord.copy(
                        selectedOptionIndex = event.index,
                        isMeaningRevealed = true
                    )
                    vocabularyList = vocabularyList.toMutableList().apply {
                        set(currentIndex, updatedWord)
                    }
//                    _uiState.value = BaseState.Success(updatedWord)
                }
            }

            is VocabularyEvent.OnNavigateToReviewScreen -> sendEffect(VocabularyEffect.NavigateToReview)
        }
    }

    private fun loadVocabularyList() {
        viewModelScope.launch {
//            _uiState.value = BaseState.Loading
            delay(500)

            vocabularyList = listOf(
                VocabularyState(
                    id = "vocab_001",
                    word = "Accommodation",
                    ukPhonetic = "/əˌkɒm.əˈdeɪ.ʃən/",
                    usPhonetic = "/əˌkɑː.məˈdeɪ.ʃən/",
                    meaning = "Nơi để ở hoặc sinh sống",
                    imageUrl = "https://img.freepik.com/free-vector/house-illustration_1284-17550.jpg",
                    level = "B1",
                    partOfSpeech = "Danh từ",
                    grammar = "accommodation for somebody/something",
                    hints = listOf("Nơi bạn ở khi đi du lịch (khách sạn, nhà nghỉ...)", "Có thể là chỗ ở tạm thời hoặc dài hạn"),
                    examples = listOf("They paid for his flights and hotel accommodation.", "The company provides accommodation for its employees."),
                    progressCurrent = 1,
                    progressTotal = 3,
                    quizOptions = listOf("Phương tiện", "Nơi ở", "Vé máy bay", "Hành lý"),
                    correctAnswerIndex = 1
                ),
                VocabularyState(
                    id = "vocab_002",
                    word = "Evaluate",
                    ukPhonetic = "/ɪˈvæl.ju.eɪt/",
                    usPhonetic = "/ɪˈvæl.ju.eɪt/",
                    meaning = "Đánh giá, định giá",
                    imageUrl = "https://img.freepik.com/free-vector/flat-hand-drawn-customer-review-illustration_23-2148889429.jpg",
                    level = "B2",
                    partOfSpeech = "Động từ",
                    grammar = "evaluate something/somebody",
                    hints = listOf("Xem xét kỹ lưỡng để đưa ra nhận xét về chất lượng hoặc giá trị", "Thường dùng trong môi trường giáo dục (chấm điểm) hoặc công việc"),
                    examples = listOf("We need to evaluate the success of the campaign.", "The students will be evaluated on their daily performance."),
                    progressCurrent = 2,
                    progressTotal = 3,
                    quizOptions = listOf("Đánh giá", "Sản xuất", "Bỏ qua", "Chấp nhận"),
                    correctAnswerIndex = 0
                ),
                VocabularyState(
                    id = "vocab_003",
                    word = "Persistent",
                    ukPhonetic = "/pəˈsɪs.tənt/",
                    usPhonetic = "/pɚˈsɪs.tənt/",
                    meaning = "Kiên trì, bền bỉ",
                    imageUrl = "https://img.freepik.com/free-vector/business-man-pushing-huge-boulder-uphill_74855-5259.jpg",
                    level = "C1",
                    partOfSpeech = "Tính từ",
                    grammar = "persistent in doing something",
                    hints = listOf("Không bỏ cuộc dù gặp rất nhiều khó khăn và thử thách", "Tiếp tục làm một việc gì đó dù bị người khác phản đối"),
                    examples = listOf("She has been persistent in her pursuit of the truth.", "Symptoms of the illness include a persistent cough."),
                    progressCurrent = 3,
                    progressTotal = 3,
                    quizOptions = listOf("Lười biếng", "Thông minh", "Nhanh nhẹn", "Kiên trì"),
                    correctAnswerIndex = 3
                )
            )
            currentIndex = 0
            updateUiWithCurrentWord()
        }
    }

    private fun updateUiWithCurrentWord() {
//        vocabularyList.getOrNull(currentIndex)?.let {
//            _uiState.value = BaseState.Success(it)
//        }
    }
}