package com.codework.daily_english.presentation.screen.lessons.sentence_builder

import java.util.UUID

data class SentenceBuilderData(
    val questions: List<SentenceBuilderQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val progressCurrent: Int = 0,
    val progressTotal: Int = 0,

    // State của câu hỏi hiện tại
    val currentQuestion: SentenceBuilderQuestion? = null,
    val selectedWords: List<WordChip> = emptyList(), // Các từ đã chọn (nằm ở vùng trả lời)
    val availableWords: List<WordChip> = emptyList(), // Các từ chưa chọn (nằm ở vùng pool)

    // UI State flags
    val isTheoryVisible: Boolean = true, // Mặc định hiện khi vào màn hình
    val isCorrect: Boolean? = null, // null: chưa check, true: đúng, false: sai
    val isCompleted: Boolean = false // Hoàn thành tất cả câu hỏi
)

data class SentenceBuilderQuestion(
    val id: String = UUID.randomUUID().toString(),
    val vietnamese: String,
    val correctSentence: List<String> = emptyList(), // Thứ tự đúng: ["I", "'d", "like", "a", "tea"]
    val grammarTitle: String = "",
    val grammarContent: String = "",
    val grammarExample: String = ""
)

data class WordChip(
    val id: String = UUID.randomUUID().toString(),
    val text: String
)