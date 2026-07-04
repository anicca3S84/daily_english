package com.codework.dailyenglish.data.dictionary.remote.dto

import com.google.gson.annotations.SerializedName

data class DefinitionDetailDto(
    @SerializedName("type") val type: String?,
    @SerializedName("aspect") val aspect: String?,
    @SerializedName("meanings") val meanings: List<MeaningGroupDto>?
)