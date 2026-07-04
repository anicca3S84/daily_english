package com.codework.dailyenglish.presentation.screen.word_detail

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.core.utils.TextToSpeechParser
import com.codework.dailyenglish.domain.dictionary.usecase.ToggleSaveWordUseCase
import com.codework.dailyenglish.domain.vocabulary.usecase.GetWordDetailUseCase
import com.codework.dailyenglish.presentation.screen.word_detail.mvi.WordDetailEffect
import com.codework.dailyenglish.presentation.screen.word_detail.mvi.WordDetailEvent
import com.codework.dailyenglish.presentation.screen.word_detail.mvi.WordDetailState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val getWordDetailUseCase: GetWordDetailUseCase,
    private val toggleSaveWordUseCase: ToggleSaveWordUseCase,
    private val textToSpeechParser: TextToSpeechParser,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<WordDetailState, WordDetailEvent, WordDetailEffect>(WordDetailState()) {

    init {
        savedStateHandle.get<String>("wordId")?.let { id ->
            onEvent(WordDetailEvent.LoadWordDetail(id))
        }
    }

    override fun onEvent(event: WordDetailEvent) {
        when (event) {
            is WordDetailEvent.LoadWordDetail -> fetchWordDetail(event.wordId)
            WordDetailEvent.OnPlayAudio -> playWordAudio()
            WordDetailEvent.OnToggleSave -> toggleSaveWord()
        }
    }

    private fun fetchWordDetail(wordId: String) {
        _state.update { it.copy(wordState = it.wordState.copy(isLoading = true, error = null)) }
        viewModelScope.launch {
            getWordDetailUseCase(wordId)
                .onSuccess { res ->
                    _state.update { it.copy(wordState = it.wordState.copy(isLoading = false, data = res)) }
                }
                .onError { err ->
                    _state.update { it.copy(wordState = it.wordState.copy(isLoading = false, error = err.asUiText())) }
                }
        }
    }

    // Hoàn thành hàm phát âm thanh từ vựng
    private fun playWordAudio() {
        val currentWord = state.value.wordState.data ?: return
        playAudio(
            url = currentWord.usAudioUrl.takeIf { it.isNotBlank() },
            word = currentWord.word,
            speed = 1f
        )
    }

    private fun playAudio(url: String?, word: String, speed: Float) {
        textToSpeechParser.playAudio(
            url = url,
            textToSpeak = word,
            speed = speed,
            scope = viewModelScope
        )
    }

    private fun toggleSaveWord() {
        val currentWord = state.value.wordState.data ?: return
        val targetSaveState = !currentWord.isSaved

        _state.update { s ->
            s.copy(wordState = s.wordState.copy(data = currentWord.copy(isSaved = targetSaveState)))
        }

        viewModelScope.launch {
            toggleSaveWordUseCase(currentWord.id)
                .onError { err ->
                    _state.update { s ->
                        s.copy(wordState = s.wordState.copy(data = currentWord.copy(isSaved = currentWord.isSaved)))
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeechParser.release()
    }
}