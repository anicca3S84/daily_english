package com.codework.dailyenglish.data.dictionary.remote.dto
import com.google.gson.annotations.SerializedName

data class WordDto(
    @SerializedName("id") val id: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("word") val word: String?,
    @SerializedName("meaning") val meaning: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("ukPhonetic") val ukPhonetic: String?,
    @SerializedName("usPhonetic") val usPhonetic: String?,
    @SerializedName("ukAudioUrl") val ukAudioUrl: String?,
    @SerializedName("usAudioUrl") val usAudioUrl: String?,
    @SerializedName("examples") val examples: List<String>?,
    @SerializedName("relatedWords") val relatedWords: List<String>?,
    @SerializedName("isSaved") val isSaved: Boolean?,
    @SerializedName("definitions") val definitions: List<DefinitionDetailDto>?
)