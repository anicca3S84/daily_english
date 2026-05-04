package com.codework.dailyenglish.data.lesson.mapper

import com.codework.dailyenglish.data.lesson.remote.dto.*
import com.codework.dailyenglish.domain.lesson.model.*
import kotlin.Boolean

fun LessonResultResponseDto.toResultDomain(): LessonResult {
    return LessonResult(
        scorePercentage = this.scorePercentage ?: 0,
        starsEarned = this.starsEarned ?: 0,
        xpEarned = this.xpEarned ?: 0
    )
}

fun LessonPlayResponseDto.toDomain(): LessonPlay {
    return LessonPlay(
        lessonId = this.lessonId.orEmpty(),
        title = this.title.orEmpty(),
        resumeCursor = this.resumeCursor ?: 0,
        steps = this.steps?.map { it.toDomain() } ?: emptyList()
    )
}


fun LessonStepDto.toDomain(): LessonStep {
    val domainStepType = when(this.stepType) {
        "VOCAB" -> StepType.VOCAB
        "QUESTION" -> StepType.QUESTION
        "THEORY" -> StepType.THEORY
        else -> StepType.UNKNOWN
    }

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
            relatedWords = dtoData.relatedWords
        )
        is QuestionDto -> {
            val domainContent = when (val dtoContent = dtoData.content) {
                is QuestionContentDto.MultipleChoice -> QuestionContent.MultipleChoice(
                    prompt = dtoContent.prompt.orEmpty(),
                    audioUrl = dtoContent.audioUrl,
                    translation = dtoContent.translation,
                    options = dtoContent.options?.map {
                        QuestionContent.MultipleChoice.Option(
                            id = it.id.orEmpty(),
                            text = it.text.orEmpty(),
                            isCorrect = it.isCorrect ?: false,
                        )
                    } ?: emptyList()
                )

                is QuestionContentDto.SentenceBuilder -> QuestionContent.SentenceBuilder(
                    prompt = dtoContent.prompt.orEmpty(),
                    correctSentence = dtoContent.correctSentence.orEmpty(),
                    words = dtoContent.words ?: emptyList(),
                )

                is QuestionContentDto.Matching -> QuestionContent.Matching(
                    prompt = dtoContent.prompt.orEmpty(),
                    pairs = dtoContent.pairs?.map {
                        QuestionContent.Matching.PairData(
                            left = it.left.orEmpty(),
                            right = it.right.orEmpty()
                        )
                    } ?: emptyList()
                )

                is QuestionContentDto.FillBlank -> QuestionContent.FillBlank(
                    prompt = dtoContent.prompt.orEmpty(),
                    sentenceWithBlank = dtoContent.sentenceWithBlank.orEmpty(),
                    correctAnswer = dtoContent.correctAnswer.orEmpty(),
                    options = dtoContent.options ?: emptyList(),
                    hintMask = dtoContent.hintMask ?: emptyList()
                )

                is QuestionContentDto.Dictation -> QuestionContent.Dictation(
                    prompt = dtoContent.prompt.orEmpty(),
                    targetWord = dtoContent.targetWord.orEmpty(),
                    hint = dtoContent.hint.orEmpty(),
                    correctAnswer = dtoContent.correctAnswer.orEmpty(),
                    acceptedAnswers = dtoContent.acceptedAnswers ?: emptyList()
                )

                is QuestionContentDto.Speaking -> QuestionContent.Speaking(
                    prompt = dtoContent.prompt.orEmpty(),
                    targetText = dtoContent.targetText.orEmpty(),
                    phonetic = dtoContent.phonetic.orEmpty(),
                    translation = dtoContent.translation.orEmpty()
                )

                else -> QuestionContent.Unknown
            }

            StepData.Question(
                id = dtoData.id.orEmpty(),
                type = dtoData.type.orEmpty(),
                content = domainContent,
                selectedOptionId = dtoData.selectedOptionId,
                isAnswerEvaluated = dtoData.selectedOptionId != null,
                userAnswer = dtoData.userAnswer.orEmpty(),
                selectedIndices = dtoData.selectedIndices ?: emptyList(),
                isCorrect = dtoData.isCorrect,
            )
        }
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