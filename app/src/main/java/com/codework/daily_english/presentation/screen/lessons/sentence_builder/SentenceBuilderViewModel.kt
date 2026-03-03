package com.codework.daily_english.presentation.screen.lessons.sentence_builder

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentenceBuilderViewModel @Inject constructor() : BaseViewModel<SentenceBuilderData>() {

    init {
        loadData()
    }

    fun onEvent(event: SentenceBuilderEvent) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        when (event) {
            is SentenceBuilderEvent.OnGoBack -> {
                viewModelScope.launch { _navigationChannel.send(Route.LessonScreen) }
            }
            
            is SentenceBuilderEvent.OnToggleTheory -> {
                _uiState.value = UiState.Success(
                    currentState.copy(isTheoryVisible = !currentState.isTheoryVisible)
                )
            }

            is SentenceBuilderEvent.OnSelectWord -> {
                // Di chuyển từ Pool -> Selected
                val newSelected = currentState.selectedWords + event.wordChip
                val newAvailable = currentState.availableWords - event.wordChip
                
                _uiState.value = UiState.Success(
                    currentState.copy(
                        selectedWords = newSelected,
                        availableWords = newAvailable,
                        isCorrect = null // Reset trạng thái check khi user thao tác
                    )
                )
            }

            is SentenceBuilderEvent.OnDeselectWord -> {
                // Di chuyển từ Selected -> Pool
                val newSelected = currentState.selectedWords - event.wordChip
                val newAvailable = currentState.availableWords + event.wordChip
                
                _uiState.value = UiState.Success(
                    currentState.copy(
                        selectedWords = newSelected,
                        availableWords = newAvailable,
                        isCorrect = null
                    )
                )
            }

            is SentenceBuilderEvent.OnCheckAnswer -> {
                val currentQ = currentState.currentQuestion ?: return
                
                // Tạo câu từ các từ đã chọn
                val userSentence = currentState.selectedWords.map { it.text }
                
                // So sánh
                val isCorrect = userSentence == currentQ.correctSentence
                
                _uiState.value = UiState.Success(currentState.copy(isCorrect = isCorrect))
            }

            is SentenceBuilderEvent.OnNextQuestion -> {
                val nextIndex = currentState.currentIndex + 1
                if (nextIndex < currentState.questions.size) {
                    prepareQuestion(currentState.questions, nextIndex)
                } else {
                    _uiState.value = UiState.Success(currentState.copy(isCompleted = true))
                    // Có thể navigate sang màn hình kết quả hoặc finish
                    viewModelScope.launch { _navigationChannel.send(Route.LessonScreen) }
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(500)

            // Mock Data 3 câu
            val questions = listOf(
                SentenceBuilderQuestion(
                    id = "q1",
                    vietnamese = "Tôi muốn một tách trà.",
                    correctSentence = listOf("I", "'d", "like", "a", "cup", "of", "tea", "please"),
                    grammarTitle = "Would like + Noun",
                    grammarContent = "We use 'would like' or 'I'd like' to politely ask for something.",
                    grammarExample = "I'd like a ticket to London, please."
                ),
                SentenceBuilderQuestion(
                    id = "q2",
                    vietnamese = "Cô ấy thích học tiếng Anh.",
                    correctSentence = listOf("She", "is", "interested", "in", "learning", "English", "please"),
                    grammarTitle = "Interested in + V-ing",
                    grammarContent = "Use 'interested in' to show you want to know or learn more about something. Follow with a Noun or Verb-ing.",
                    grammarExample = "Are you interested in playing soccer?"
                ),
                SentenceBuilderQuestion(
                    id = "q3",
                    vietnamese = "Có rất nhiều người trong công viên.",
                    correctSentence = listOf("There", "are", "many", "people", "in", "the", "park", "please","please","please","please" ),
                    grammarTitle = "There is / There are",
                    grammarContent = "Use 'There are' for countable plural nouns (many people, two cats).",
                    grammarExample = "There are five apples on the table."
                )
            )

            prepareQuestion(questions, 0)
        }
    }

    private fun prepareQuestion(questions: List<SentenceBuilderQuestion>, index: Int) {
        val currentQ = questions[index]
        
        // Tạo WordChips và trộn ngẫu nhiên cho availableWords
        val wordChips = currentQ.correctSentence.map { WordChip(text = it) }.shuffled()

        _uiState.value = UiState.Success(
            SentenceBuilderData(
                questions = questions,
                currentIndex = index,
                progressCurrent = index + 1,
                progressTotal = questions.size,
                currentQuestion = currentQ,
                selectedWords = emptyList(),
                availableWords = wordChips,
                isTheoryVisible = true, // Hiện lý thuyết mỗi khi qua câu mới
                isCorrect = null
            )
        )
    }
}