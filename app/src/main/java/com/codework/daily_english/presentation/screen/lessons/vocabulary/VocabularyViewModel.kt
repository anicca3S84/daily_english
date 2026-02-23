package com.codework.daily_english.presentation.screen.lessons.vocabulary
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyViewModel @Inject constructor() : BaseViewModel<VocabularyData>() {

    private var vocabularyList = listOf<VocabularyData>()
    private var currentIndex = 0

    init {
        loadVocabularyList()
    }

    fun onEvent(event: VocabularyEvent) {
        when (event) {
            is VocabularyEvent.OnPlayAudio -> { /* Logic Audio */
            }

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
                    viewModelScope.launch { _navigationChannel.send(Route.LessonScreen) }
                }
            }

            // XỬ LÝ CLICK XEM GỢI Ý
            is VocabularyEvent.OnRevealHint -> {
                val currentWord = vocabularyList.getOrNull(currentIndex) ?: return
                if (!currentWord.isHintRevealed) {
                    val updatedWord = currentWord.copy(isHintRevealed = true)
                    vocabularyList = vocabularyList.toMutableList().apply {
                        set(currentIndex, updatedWord)
                    }
                    _uiState.value = UiState.Success(updatedWord)
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
                    _uiState.value = UiState.Success(updatedWord)
                }
            }

            is VocabularyEvent.OnNavigateToReviewScreen -> {
                navigateTo(Route.ReviewVocabularyScreen)
            }
        }
    }

    private fun loadVocabularyList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(500)

            // Cập nhật Mock Data với các từ KHÁC NHAU để kiểm tra
            vocabularyList = listOf(
                // --- TỪ SỐ 1: ACCOMMODATION (Danh từ) ---
                VocabularyData(
                    id = "vocab_001",
                    word = "Accommodation",
                    ukPhonetic = "/əˌkɒm.əˈdeɪ.ʃən/",
                    usPhonetic = "/əˌkɑː.məˈdeɪ.ʃən/",
                    meaning = "Nơi để ở hoặc sinh sống",
                    imageUrl = "https://img.freepik.com/free-vector/house-illustration_1284-17550.jpg",
                    level = "B1",
                    partOfSpeech = "Danh từ",
                    grammar = "accommodation for somebody/something",
                    hints = listOf(
                        "Nơi bạn ở khi đi du lịch (khách sạn, nhà nghỉ...)",
                        "Có thể là chỗ ở tạm thời hoặc dài hạn"
                    ),
                    examples = listOf(
                        "They paid for his flights and hotel accommodation.",
                        "The company provides accommodation for its employees."
                    ),
                    relatedWords = emptyList(),
                    progressCurrent = 1,
                    progressTotal = 3,
                    quizOptions = listOf("Phương tiện", "Nơi ở", "Vé máy bay", "Hành lý"),
                    correctAnswerIndex = 1, // Đáp án đúng nằm ở vị trí thứ 2
                    isHintRevealed = false,
                    selectedOptionIndex = null,
                    isMeaningRevealed = false
                ),

                // --- TỪ SỐ 2: EVALUATE (Động từ) ---
                VocabularyData(
                    id = "vocab_002",
                    word = "Evaluate",
                    ukPhonetic = "/ɪˈvæl.ju.eɪt/",
                    usPhonetic = "/ɪˈvæl.ju.eɪt/",
                    meaning = "Đánh giá, định giá",
                    imageUrl = "https://img.freepik.com/free-vector/flat-hand-drawn-customer-review-illustration_23-2148889429.jpg",
                    level = "B2",
                    partOfSpeech = "Động từ",
                    grammar = "evaluate something/somebody",
                    hints = listOf(
                        "Xem xét kỹ lưỡng để đưa ra nhận xét về chất lượng hoặc giá trị",
                        "Thường dùng trong môi trường giáo dục (chấm điểm) hoặc công việc"
                    ),
                    examples = listOf(
                        "We need to evaluate the success of the campaign.",
                        "The students will be evaluated on their daily performance."
                    ),
                    relatedWords = emptyList(),
                    progressCurrent = 2,
                    progressTotal = 3,
                    quizOptions = listOf("Đánh giá", "Sản xuất", "Bỏ qua", "Chấp nhận"),
                    correctAnswerIndex = 0, // Đáp án đúng nằm ở vị trí đầu tiên
                    isHintRevealed = false,
                    selectedOptionIndex = null,
                    isMeaningRevealed = false
                ),

                // --- TỪ SỐ 3: PERSISTENT (Tính từ) ---
                VocabularyData(
                    id = "vocab_003",
                    word = "Persistent",
                    ukPhonetic = "/pəˈsɪs.tənt/",
                    usPhonetic = "/pɚˈsɪs.tənt/",
                    meaning = "Kiên trì, bền bỉ",
                    imageUrl = "https://img.freepik.com/free-vector/business-man-pushing-huge-boulder-uphill_74855-5259.jpg",
                    level = "C1",
                    partOfSpeech = "Tính từ",
                    grammar = "persistent in doing something",
                    hints = listOf(
                        "Không bỏ cuộc dù gặp rất nhiều khó khăn và thử thách",
                        "Tiếp tục làm một việc gì đó dù bị người khác phản đối"
                    ),
                    examples = listOf(
                        "She has been persistent in her pursuit of the truth.",
                        "Symptoms of the illness include a persistent cough."
                    ),
                    relatedWords = emptyList(),
                    progressCurrent = 3,
                    progressTotal = 3,
                    quizOptions = listOf("Lười biếng", "Thông minh", "Nhanh nhẹn", "Kiên trì"),
                    correctAnswerIndex = 3, // Đáp án đúng nằm ở vị trí cuối cùng
                    isHintRevealed = false,
                    selectedOptionIndex = null,
                    isMeaningRevealed = false
                )
            )
            currentIndex = 0
            updateUiWithCurrentWord()
        }
    }

    private fun updateUiWithCurrentWord() {
        vocabularyList.getOrNull(currentIndex)?.let {
            _uiState.value = UiState.Success(it)
        }
    }
}