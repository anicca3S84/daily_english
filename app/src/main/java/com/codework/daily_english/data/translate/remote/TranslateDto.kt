package com.codework.daily_english.data.translate.remote

import com.google.gson.annotations.SerializedName

data class TranslateDto(
    @SerializedName("translatedText")
    val translatedText: String
)