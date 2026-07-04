package com.codework.dailyenglish.domain.dictionary.model

data class DefinitionDetail(
    val type: String,
    val aspect: String,
    val meanings: List<MeaningGroup>
)