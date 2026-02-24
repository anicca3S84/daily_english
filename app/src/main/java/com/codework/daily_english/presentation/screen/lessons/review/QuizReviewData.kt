package com.codework.daily_english.presentation.screen.lessons.review

import com.codework.daily_english.presentation.model.QuizType

data class QuizReviewData(
    val id: String,
    val sentence: String, // Ví dụ: "The company provides ________ for its employees."
    val translatedSentence: String, // Dịch nghĩa phụ trợ khi hiện đáp án
    val options: List<String>, // Danh sách 4 từ vựng
    val correctAnswerIndex: Int,
    val selectedOptionIndex: Int? = null,
    val isAnswerRevealed: Boolean = false,
    val progressCurrent: Int,
    val progressTotal: Int,
    val type: QuizType = QuizType.VOCABULARY, // Thêm dòng này
)