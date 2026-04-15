package com.codework.dailyenglish.presentation.screen.lessons.speaking.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val translatedText: String? = null,
    val isTranslated: Boolean = false
)