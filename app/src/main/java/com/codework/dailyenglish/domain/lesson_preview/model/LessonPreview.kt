package com.codework.dailyenglish.domain.lesson_preview.model

data class LessonPreview(
    val id: String,
    val title: String,
    val subtitle: String,
    val content: String,
    val description: String,
    val vocabCount: Int,
    val grammarCount: Int,
    val hasVocab: Boolean,
    val hasGrammar: Boolean,
    val hasListening: Boolean,
    val hasSpeaking: Boolean,
    val xpReward: Int,
    val outcomes: List<String>
)