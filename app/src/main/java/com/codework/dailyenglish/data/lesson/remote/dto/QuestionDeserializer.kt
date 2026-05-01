package com.codework.dailyenglish.data.lesson.remote.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class QuestionDeserializer : JsonDeserializer<QuestionDto> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): QuestionDto {
        val jsonObject = json.asJsonObject

        val id = jsonObject.get("id")?.asString
        val type = jsonObject.get("type")?.asString
        val selectedOptionId = jsonObject.get("selectedOptionId")?.takeUnless { it.isJsonNull }?.asString
        val contentElement = jsonObject.get("content")

        val parsedContent: QuestionContentDto? = if (contentElement != null && !contentElement.isJsonNull) {
            when (type) {
                "MULTIPLE_CHOICE" -> context.deserialize(contentElement, QuestionContentDto.MultipleChoice::class.java)
                "SENTENCE_BUILDER" -> context.deserialize(contentElement, QuestionContentDto.SentenceBuilder::class.java)
                "MATCHING" -> context.deserialize(contentElement, QuestionContentDto.Matching::class.java)
                "FILL_BLANK" -> context.deserialize(contentElement, QuestionContentDto.FillBlank::class.java)
                "DICTATION" -> context.deserialize(contentElement, QuestionContentDto.Dictation::class.java)
                "SPEAKING" -> context.deserialize(contentElement, QuestionContentDto.Speaking::class.java)
                else -> null
            }
        } else {
            null
        }

        return QuestionDto(
            id = id,
            type = type,
            content = parsedContent,
            selectedOptionId = selectedOptionId
        )
    }
}