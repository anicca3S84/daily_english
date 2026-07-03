package com.codework.dailyenglish.data.vocabulary.remote.dto

import com.google.gson.annotations.SerializedName

data class VocabTopicDto(
    @SerializedName("publicId") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("totalWords") val totalWords: Int?,
)