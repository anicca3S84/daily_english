package com.codework.dailyenglish.presentation.screen.word_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
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

    private fun playWordAudio() {
        val audioUrl = state.value.wordState.data?.ukPhonetic ?: return
    }

    private fun toggleSaveWord() {
        val currentWord = state.value.wordState.data ?: return
        _state.update { s ->
            s.copy(wordState = s.wordState.copy(data = currentWord.copy(isSaved = !currentWord.isSaved)))
        }
    }
}