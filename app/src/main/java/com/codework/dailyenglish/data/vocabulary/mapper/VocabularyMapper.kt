package com.codework.dailyenglish.data.vocabulary.mapper

import com.codework.dailyenglish.data.dictionary.remote.dto.WordDto
import com.codework.dailyenglish.domain.dictionary.model.Word

fun WordDto.toDomain(): Word {
    return Word(
        id = this.id.orEmpty(),
        type = this.type.orEmpty(),
        word = this.word.orEmpty(),
        meaning = this.meaning.orEmpty(),
        imageUrl = this.imageUrl.orEmpty(),
        ukPhonetic = this.ukPhonetic.orEmpty(),
        usPhonetic = this.usPhonetic.orEmpty(),
        ukAudioUrl = this.ukAudioUrl.orEmpty(),
        usAudioUrl = this.usAudioUrl.orEmpty(),
        examples = this.examples ?: emptyList(),
        relatedWords = this.relatedWords ?: emptyList(),
        isSaved = this.isSaved ?: false
    )
}