package com.codework.dailyenglish.data.lesson.mapper

import com.codework.dailyenglish.data.lesson.remote.dto.*
import com.codework.dailyenglish.domain.lesson.model.*

fun LessonPlayResponseDto.toDomain(): LessonPlay {
    return LessonPlay(
        lessonId = this.lessonId.orEmpty(),
        title = this.title.orEmpty(),
        resumeCursor = this.resumeCursor ?: 0,
        steps = this.steps?.map { it.toDomain() } ?: emptyList()
    )
}

fun LessonStepDto.toDomain(): LessonStep {
    // 1. Map StepType (String -> Enum)
    val domainStepType = when(this.stepType) {
        "VOCAB" -> StepType.VOCAB
        "QUESTION" -> StepType.QUESTION
        "THEORY" -> StepType.THEORY
        else -> StepType.UNKNOWN
    }

    // 2. Map StepData (DTO Polymorphic -> Domain Sealed Class)
    val domainData: StepData = when (val dtoData = this.data) {
        is VocabDto -> StepData.Vocab(
            word = dtoData.word.orEmpty(),
            meaning = dtoData.meaning.orEmpty(),
            imageUrl = dtoData.imageUrl,
            ukPhonetic = dtoData.ukPhonetic,
            usPhonetic = dtoData.usPhonetic,
            ukAudioUrl = dtoData.ukAudioUrl,
            usAudioUrl = dtoData.usAudioUrl,
            examples = dtoData.examples,
            suggests = dtoData.suggests
        )
        is QuestionDto -> StepData.Question(
            content = dtoData.content.orEmpty(),
            selectedOptionId = dtoData.selectedOptionId,
            type = dtoData.type.orEmpty(),
            options = dtoData.options?.map {
                StepData.Question.Option(
                    id = it.id.orEmpty(),
                    content = it.content.orEmpty(),
                    isCorrect = it.isCorrect ?: false
                )
            } ?: emptyList()
        )
        is TheoryDto -> StepData.Theory(
            title = dtoData.title,
            content = dtoData.content.orEmpty()
        )
        else -> StepData.Unknown
    }

    return LessonStep(
        stepId = this.stepId ?: 0L,
        refId = this.refId.orEmpty(),
        stepType = domainStepType,
        orderIndex = this.orderIndex ?: 0,
        data = domainData
    )
}