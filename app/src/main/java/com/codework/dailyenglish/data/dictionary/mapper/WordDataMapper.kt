package com.codework.dailyenglish.data.dictionary.mapper
import com.codework.dailyenglish.data.dictionary.remote.dto.WordDto
import com.codework.dailyenglish.domain.dictionary.model.Word

fun WordDto.toDomain(): Word {
    return Word(
        id = this.id.orEmpty(),
        word = this.word.orEmpty(),
        type = this.type.orEmpty(),
        ukPhonetic = this.ukPhonetic.orEmpty(),
        usPhonetic = this.usPhonetic.orEmpty(),
        meaning = this.meaning.orEmpty(),
        imageUrl = this.imageUrl.orEmpty(),
        ukAudioUrl = this.ukAudioUrl.orEmpty(),
        usAudioUrl = this.usAudioUrl.orEmpty(),
        examples = this.examples ?: emptyList(),
        relatedWords = this.relatedWords ?: emptyList(),
        isSaved = this.isSaved ?: false
    )
}