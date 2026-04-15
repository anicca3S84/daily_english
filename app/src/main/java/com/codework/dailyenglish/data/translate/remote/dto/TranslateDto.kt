package com.codework.dailyenglish.data.translate.remote.dto

import com.google.gson.annotations.SerializedName

data class TranslateDto(
    @SerializedName("translatedText")
    val translatedText: String
)