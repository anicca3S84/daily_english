package com.codework.dailyenglish.data.dictionary.mapper
import com.codework.dailyenglish.data.dictionary.remote.dto.DefinitionDetailDto
import com.codework.dailyenglish.data.dictionary.remote.dto.MeaningGroupDto
import com.codework.dailyenglish.data.dictionary.remote.dto.WordDto
import com.codework.dailyenglish.domain.dictionary.model.DefinitionDetail
import com.codework.dailyenglish.domain.dictionary.model.MeaningGroup
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
        isSaved = this.isSaved ?: false,
        definitions = this.definitions?.map { it.toDomain() } ?: emptyList()
    )
}

fun DefinitionDetailDto.toDomain() = DefinitionDetail(
    type = this.type.orEmpty(),
    aspect = this.aspect.orEmpty(),
    meanings = this.meanings?.map { it.toDomain() } ?: emptyList()
)

fun MeaningGroupDto.toDomain() = MeaningGroup(
    explanation = this.explanation.orEmpty(),
    description = this.description.orEmpty(),
    examples = this.examples ?: emptyList()
)