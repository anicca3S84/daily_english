package com.codework.dailyenglish.data.translate.remote.dto

data class TranslateRequestDto(
    val q: String,
    val source: String,
    val target: String
)