package com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi

import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryEvent

sealed interface LessonEvent {
    data class LoadLesson(val lessonId: String) : LessonEvent
    data object OnNextStep : LessonEvent
    data object OnPreviousStep : LessonEvent
    data class OnAnswerInput(
        val answer: String,
        val indices: List<Int> = emptyList()
    ) : LessonEvent
    data object OnCheckAnswerClicked : LessonEvent
    data class OnPlayAudio(val url: String?, val word: String, val speed: Float = 1f) : LessonEvent
    data object OnQuitClick : LessonEvent
    data object OnDismissQuitDialog : LessonEvent
    data object OnConfirmQuit : LessonEvent
    data class OnToggleSaveWord(val wordId: String) : LessonEvent
    object OnStartSpeech : LessonEvent
    object OnStopSpeech : LessonEvent

}