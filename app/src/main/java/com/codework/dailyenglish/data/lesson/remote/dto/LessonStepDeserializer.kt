package com.codework.dailyenglish.data.lesson.remote.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class LessonStepDeserializer : JsonDeserializer<LessonStepDto> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LessonStepDto {
        val jsonObject = json.asJsonObject

        fun getSafeString(key: String): String? {
            val element = jsonObject.get(key)
            return if (element != null && !element.isJsonNull) element.asString else null
        }

        fun getSafeLong(key: String): Long? {
            val element = jsonObject.get(key)
            return if (element != null && !element.isJsonNull) element.asLong else null
        }

        fun getSafeInt(key: String): Int? {
            val element = jsonObject.get(key)
            return if (element != null && !element.isJsonNull) element.asInt else null
        }

        val stepType = getSafeString("stepType") ?: "UNKNOWN"
        val dataElement = jsonObject.get("data")

        val parsedData: StepDataDto? = if (dataElement != null && !dataElement.isJsonNull) {
            when (stepType) {
                "VOCAB" -> context.deserialize(dataElement, VocabDto::class.java)
                "QUESTION" -> context.deserialize(dataElement, QuestionDto::class.java)
                "THEORY" -> context.deserialize(dataElement, TheoryDto::class.java)
                else -> null
            }
        } else {
            null
        }

        return LessonStepDto(
            stepId = getSafeLong("stepId"),
            stepType = stepType,
            orderIndex = getSafeInt("orderIndex"),
            refId = getSafeString("refId"),
            data = parsedData
        )
    }
}