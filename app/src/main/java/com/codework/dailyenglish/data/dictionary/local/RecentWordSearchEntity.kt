package com.codework.dailyenglish.data.dictionary.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_word_searches")
data class RecentWordSearchEntity(
    @PrimaryKey
    val wordId: String, // ID của từ (dùng kiểu String hoặc Long tùy dự án của bạn)
    val wordText: String, // Từ tiếng Anh hiển thị ra UI
    val timestamp: Long // Thời gian click vào từ
)