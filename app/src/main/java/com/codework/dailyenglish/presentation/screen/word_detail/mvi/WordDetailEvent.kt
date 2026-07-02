package com.codework.dailyenglish.presentation.screen.word_detail.mvi

sealed interface WordDetailEvent {
    data class LoadWordDetail(val wordId: String) : WordDetailEvent
    object OnPlayAudio : WordDetailEvent
    object OnToggleSave : WordDetailEvent
}