package com.codework.dailyenglish.presentation.screen.dictionary

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.base.BaseEffect.ShowToast
import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.core.utils.TextToSpeechParser
import com.codework.dailyenglish.domain.dictionary.usecase.ClearRecentSearchesUseCase
import com.codework.dailyenglish.domain.dictionary.usecase.DeleteRecentSearchUseCase
import com.codework.dailyenglish.domain.dictionary.usecase.GetRecentSearchesUseCase
import com.codework.dailyenglish.domain.dictionary.usecase.SaveRecentSearchUseCase
import com.codework.dailyenglish.domain.dictionary.usecase.ToggleSaveWordUseCase
import com.codework.dailyenglish.domain.dictionary.usecase.SearchWordUseCase
import com.codework.dailyenglish.domain.vocabulary.usecase.GetVocabTopicsUseCase
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryEffect
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryEvent
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryState
import com.codework.dailyenglish.presentation.utils.UiText
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val searchWordUseCase: SearchWordUseCase,
    private val toggleSaveWordUseCase: ToggleSaveWordUseCase,
    private val getVocabTopicsUseCase: GetVocabTopicsUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase,
    private val clearRecentSearchesUseCase: ClearRecentSearchesUseCase,
    private val textToSpeechParser: TextToSpeechParser,
) : BaseViewModel<DictionaryState, DictionaryEvent, DictionaryEffect>(
    initialState = DictionaryState()
) {

    private var searchJob: Job? = null


    init {
        fetchVocabTopics()
        observeRecentSearches()
    }

    private fun observeRecentSearches() {
        viewModelScope.launch {
            getRecentSearchesUseCase().collect { list ->
                _state.update { it.copy(recentSearches = list) }
            }
        }
    }

    private fun fetchVocabTopics() {
        _state.update { it.copy(topicsState = it.topicsState.copy(isLoading = true, error = null)) }
        viewModelScope.launch {
            getVocabTopicsUseCase()
                .onSuccess { topics ->
                    _state.update {
                        it.copy(
                            topicsState = it.topicsState.copy(
                                data = topics,
                                isLoading = false
                            )
                        )
                    }
                }
                .onError { err ->
                    _state.update {
                        it.copy(
                            topicsState = it.topicsState.copy(
                                isLoading = false,
                                error = err.asUiText()
                            )
                        )
                    }
                }
        }
    }

    override fun onEvent(event: DictionaryEvent) {
        when (event) {
            is DictionaryEvent.LoadTopics -> {
                _state.update { it.copy(topicsState = it.topicsState.copy(isLoading = true)) }
                viewModelScope.launch {
                    getVocabTopicsUseCase()
                        .onSuccess { topics ->
                            _state.update { it.copy(topicsState = it.topicsState.copy(data = topics, isLoading = false)) }
                        }
                        .onError { err ->
                            _state.update { it.copy(topicsState = it.topicsState.copy(isLoading = false, error = err.asUiText())) }
                        }
                }
            }

            is DictionaryEvent.OnSearchQueryChange -> {
                val query = event.query
                _state.update { it.copy(query = query) }
                searchJob?.cancel()

                if (query.isBlank()) {
                    _state.update { it.copy(searchResults = BaseUiState()) }
                    return
                }

                _state.update {
                    it.copy(searchResults = it.searchResults.copy(isLoading = true, error = null))
                }

                searchJob = viewModelScope.launch {
                    searchWordUseCase(query)
                        .onSuccess { domainWords ->
                            _state.update {
                                it.copy(
                                    searchResults = it.searchResults.copy(
                                        data = domainWords,
                                        isLoading = false,
                                        error = null
                                    )
                                )
                            }
                        }
                        .onError { err ->
                            _state.update {
                                it.copy(
                                    searchResults = it.searchResults.copy(
                                        isLoading = false,
                                        error = err.asUiText()
                                    )
                                )
                            }
                            sendGlobalEffect(ShowToast(err.asUiText()))
                        }
                }
            }

            is DictionaryEvent.OnClearSearch -> {
                searchJob?.cancel()
                _state.update {
                    it.copy(
                        query = "",
                        searchResults = BaseUiState()
                    )
                }
            }

            is DictionaryEvent.OnPlayAudio -> {
                    playAudio( event.word.usAudioUrl, event.word.word, 1.0f)
            }

            is DictionaryEvent.OnToggleSaveWord -> {
                viewModelScope.launch {
                    val targetWord = event.word
                    val newSaveStatus = !targetWord.isSaved

                    _state.update { currentState ->
                        val currentList = currentState.searchResults.data ?: emptyList()
                        val updatedList = currentList.map { word ->
                            if (word.id == targetWord.id) {
                                word.copy(isSaved = newSaveStatus)
                            } else {
                                word
                            }
                        }
                        currentState.copy(
                            searchResults = currentState.searchResults.copy(data = updatedList)
                        )
                    }

                    toggleSaveWordUseCase(targetWord.id)
                        .onSuccess {
                            sendGlobalEffect(
                                ShowToast(UiText.StringResource(R.string.noti_success))
                            )
                        }
                        .onError { err ->
                            val uiError = err.asUiText()
                            _state.update { currentState ->
                                val currentList = currentState.searchResults.data ?: emptyList()
                                val rollbackList = currentList.map { word ->
                                    if (word.id == targetWord.id) {
                                        word.copy(isSaved = targetWord.isSaved)
                                    } else {
                                        word
                                    }
                                }
                                currentState.copy(
                                    searchResults = currentState.searchResults.copy(data = rollbackList)
                                )
                            }
                            sendGlobalEffect(ShowToast(uiError))
                        }
                }
            }

            is DictionaryEvent.OnQuickActionClick -> {}

            is DictionaryEvent.OnTopicClick -> {
                sendEffect(
                    DictionaryEffect.NavigateToTopicWords(
                        topicId = event.topicId,
                        topicName = event.topicName
                    )
                )
            }

            is DictionaryEvent.OnWordClick -> {
                val clickedWord = event.word

                viewModelScope.launch {
                    // Click phát là lưu luôn từ này vào Local DB
                    saveRecentSearchUseCase(
                        wordId = clickedWord.id,
                        wordText = clickedWord.word
                    )
                }

                _state.update {
                    it.copy(
                        query = "",
                        searchResults = BaseUiState()
                    )
                }

                sendEffect(
                    DictionaryEffect.NavigateToWordDetail(
                        wordId = clickedWord.id
                    )
                )
            }

            is DictionaryEvent.OnExploreTopicsClick -> {
                sendEffect(DictionaryEffect.NavigateToTopicSelection(origin = "DICTIONARY"))
            }

            // --- XỬ LÝ CÁC EVENT MỚI ĐƯỢC THÊM VÀO ---

            is DictionaryEvent.OnRecentSearchClick -> {
                // Điều hướng thẳng sang màn detail khi click từ lịch sử
                sendEffect(
                    DictionaryEffect.NavigateToWordDetail(
                        wordId = event.recentWord.wordId
                    )
                )
            }

            is DictionaryEvent.OnDeleteRecentSearch -> {
                viewModelScope.launch {
                    // Xóa từ này trong Room DB local
                    deleteRecentSearchUseCase(event.wordId)
                }
            }

            is DictionaryEvent.OnClearRecentSearches -> {
                viewModelScope.launch {
                    // Xóa toàn bộ lịch sử trong Room DB local
                    clearRecentSearchesUseCase()
                }
            }

            is DictionaryEvent.OnWordOfTheDayClick -> {
                // Điều hướng sang màn detail của từ "Serendipity"
                sendEffect(
                    DictionaryEffect.NavigateToWordDetail(
                        wordId = "serendipity_id" // Thay bằng ID thực tế nếu cần
                    )
                )
            }
        }
    }

    private fun playAudio(url: String?, word: String, speed: Float) {
        textToSpeechParser.playAudio(
            url = url,
            textToSpeak = word,
            speed = speed,
            scope = viewModelScope
        )
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeechParser.release()
    }
}