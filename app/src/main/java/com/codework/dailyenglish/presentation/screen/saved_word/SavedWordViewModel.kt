package com.codework.dailyenglish.presentation.screen.saved_word

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.base.BaseEffect.ShowToast
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.dictionary.usecase.GetSavedWordUseCase
import com.codework.dailyenglish.domain.dictionary.usecase.ToggleSaveWordUseCase
import com.codework.dailyenglish.presentation.screen.courses.mapper.toUiModels
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryEffect
import com.codework.dailyenglish.presentation.screen.saved_word.mvi.SavedWordsEffect
import com.codework.dailyenglish.presentation.screen.saved_word.mvi.SavedWordsEvent
import com.codework.dailyenglish.presentation.screen.saved_word.mvi.SavedWordsState
import com.codework.dailyenglish.presentation.utils.UiText
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedWordsViewModel @Inject constructor(
    private val getSavedWordUseCase: GetSavedWordUseCase,
    private val toggleSaveWordUseCase: ToggleSaveWordUseCase

) : BaseViewModel<SavedWordsState, SavedWordsEvent, SavedWordsEffect>(
    initialState = SavedWordsState()
) {
    init {
        loadSavedWords()
    }

    override fun onEvent(event: SavedWordsEvent) {
        when (event) {
            is SavedWordsEvent.OnPlayAudio -> {}
            is SavedWordsEvent.OnRemoveClick -> {
                _state.update { currentState ->
                    val updatedList = currentState.savedWords.data?.filter { w ->
                        w.word != event.word.word
                    }
                    currentState.copy(
                        savedWords = currentState.savedWords.copy(data = updatedList)
                    )
                }

                viewModelScope.launch {
                    val targetWord = event.word
                    val newSaveStatus = !targetWord.isSaved

                    _state.update { currentState ->
                        val currentList = currentState.savedWords.data ?: emptyList()
                        val updatedList = currentList.map { word ->
                            if (word.id == targetWord.id) word.copy(isSaved = newSaveStatus) else word
                        }
                        currentState.copy(
                            savedWords = currentState.savedWords.copy(data = updatedList)
                        )
                    }

                    toggleSaveWordUseCase(targetWord.id)
                        .onSuccess {
                            sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_success)))
                        }
                        .onError { err ->
                            val uiError = err.asUiText()

                            _state.update { currentState ->
                                val currentList = currentState.savedWords.data ?: emptyList()
                                val rollbackList = currentList.map { word ->
                                    if (word.id == targetWord.id) word.copy(isSaved = targetWord.isSaved) else word
                                }
                                currentState.copy(
                                    savedWords = currentState.savedWords.copy(data = rollbackList)
                                )
                            }
                            sendGlobalEffect(ShowToast(uiError))
                        }
                }

            }
            is SavedWordsEvent.OnFlashCardClick -> {
                sendEffect(SavedWordsEffect.NavigateToFlashCard)
            }
        }
    }

    private fun loadSavedWords() {
        _state.update { it.copy(savedWords = it.savedWords.copy(isLoading = true, error = null)) }

        viewModelScope.launch {
            getSavedWordUseCase()
                .onSuccess { result ->
                    _state.update { state ->
                        state.copy(
                            savedWords = state.savedWords.copy(
                                isLoading = false,
                                data = result
                            ),
                        )
                    }
                }
                .onError { appError ->
                    _state.update {
                        it.copy(
                            savedWords = it.savedWords.copy(
                                isLoading = false,
                                error = appError.asUiText()
                            )
                        )
                    }
                }
        }
    }
}