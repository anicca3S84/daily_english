package com.codework.dailyenglish.data.dictionary.remote.dto

import com.google.gson.annotations.SerializedName

data class MeaningGroupDto(
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("examples") val examples: List<String>?
)