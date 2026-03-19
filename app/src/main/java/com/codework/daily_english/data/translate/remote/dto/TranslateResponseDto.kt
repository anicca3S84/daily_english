package com.codework.daily_english.data.translate.remote.dto

import com.google.gson.annotations.SerializedName

data class TranslateResponseDto(
    @SerializedName("translatedText")
    val translatedText: String
)