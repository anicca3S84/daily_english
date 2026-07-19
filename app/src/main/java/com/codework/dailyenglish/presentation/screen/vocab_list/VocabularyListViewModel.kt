package com.codework.dailyenglish.presentation.screen.vocab_list

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.data.vocabulary.local.FlashcardDataStore
import com.codework.dailyenglish.domain.dictionary.usecase.GetSavedWordUseCase
import com.codework.dailyenglish.domain.dictionary.usecase.ToggleSaveWordUseCase
import com.codework.dailyenglish.presentation.screen.vocab_list.mvi.VocabularyListEffect
import com.codework.dailyenglish.presentation.screen.vocab_list.mvi.VocabularyListEvent
import com.codework.dailyenglish.presentation.screen.vocab_list.mvi.VocabularyListState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.SavedStateHandle
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.core.utils.TextToSpeechParser
import com.codework.dailyenglish.domain.vocabulary.usecase.GetVocabulariesByTopicUseCase
import com.codework.dailyenglish.presentation.utils.UiText


@HiltViewModel
class VocabularyListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSavedWordUseCase: GetSavedWordUseCase,
    private val getVocabulariesByTopicUseCase: GetVocabulariesByTopicUseCase,
    private val toggleSaveWordUseCase: ToggleSaveWordUseCase,
    private val flashcardDataStore: FlashcardDataStore,
    private val textToSpeechParser: TextToSpeechParser,
) : BaseViewModel<VocabularyListState, VocabularyListEvent, VocabularyListEffect>(VocabularyListState()) {

    private var topicId: String? = null

    init {
        topicId = savedStateHandle.get<String>("topicId")
        val topicName = savedStateHandle.get<String>("topicName")

        if (topicId != null && topicName != null) {
            _state.update { it.copy(title = topicName, isSavedMode = false) }
            loadTopicWords(topicId!!)
        } else {
            _state.update { it.copy(title = "Saved Words", isSavedMode = true) }
            loadSavedWords()
        }
    }

    private fun loadSavedWords() {
        _state.update { it.copy(
            vocabWords = it.vocabWords.copy(isLoading = true, error = null),
            isInitialized = false // <-- Reset về false khi bắt đầu tải
        )}
        viewModelScope.launch {
            getSavedWordUseCase()
                .onSuccess { resultList ->
                    _state.update { it.copy(
                        vocabWords = it.vocabWords.copy(isLoading = false, data = resultList),
                        selectedWordIds = resultList.map { word -> word.id }.toSet(),
                        isInitialized = true // <-- Đặt thành true khi tải xong thành công
                    )}
                }
                .onError { err ->
                    _state.update { it.copy(
                        vocabWords = it.vocabWords.copy(isLoading = false, error = err.asUiText()),
                        isInitialized = true // <-- Đặt thành true khi kết thúc quá trình gọi API (dù lỗi)
                    )}
                }
        }
    }

    private fun loadTopicWords(id: String) {
        _state.update { it.copy(
            vocabWords = it.vocabWords.copy(isLoading = true, error = null),
            isInitialized = false // <-- Reset về false khi bắt đầu tải
        )}
        viewModelScope.launch {
            getVocabulariesByTopicUseCase(id)
                .onSuccess { resultList ->
                    _state.update { it.copy(
                        vocabWords = it.vocabWords.copy(isLoading = false, data = resultList),
                        selectedWordIds = resultList.map { word -> word.id }.toSet(),
                        isInitialized = true // <-- Đặt thành true khi tải xong thành công
                    )}
                }
                .onError { err ->
                    _state.update { it.copy(
                        vocabWords = it.vocabWords.copy(isLoading = false, error = err.asUiText()),
                        isInitialized = true // <-- Đặt thành true khi kết thúc quá trình gọi API (dù lỗi)
                    )}
                }
        }
    }

    override fun onEvent(event: VocabularyListEvent) {
        when (event) {
            is VocabularyListEvent.OnSelectWordToggle -> {
                _state.update { currentState ->
                    val currentSelected = currentState.selectedWordIds.toMutableSet()
                    if (currentSelected.contains(event.wordId)) {
                        currentSelected.remove(event.wordId)
                    } else {
                        currentSelected.add(event.wordId)
                    }
                    currentState.copy(selectedWordIds = currentSelected)
                }
            }

            is VocabularyListEvent.OnRefresh -> {
                val currentTopicId = topicId
                if (currentTopicId != null) {
                    loadTopicWords(currentTopicId)
                } else {
                    loadSavedWords()
                }
            }

            is VocabularyListEvent.OnSelectAllToggle -> {
                _state.update { currentState ->
                    val allIds = currentState.vocabWords.data?.map { it.id } ?: emptyList()
                    val newSelected = if (currentState.selectedWordIds.size == allIds.size) {
                        emptySet()
                    } else {
                        allIds.toSet()
                    }
                    currentState.copy(selectedWordIds = newSelected)
                }
            }

            is VocabularyListEvent.OnWordClick -> {
                sendEffect(VocabularyListEffect.NavigateToWordDetail(event.wordId))
            }

            is VocabularyListEvent.OnToggleSaveClick -> {
                val targetWord = event.word
                val isSavedMode = _state.value.isSavedMode

                _state.update { currentState ->
                    val currentList = currentState.vocabWords.data ?: emptyList()
                    val updatedList = if (isSavedMode) {
                        currentList.filter { it.id != targetWord.id }
                    } else {
                        currentList.map { if (it.id == targetWord.id) it.copy(isSaved = !it.isSaved) else it }
                    }

                    // Cập nhật lại danh sách ID đã chọn
                    val updatedSelectedIds = currentState.selectedWordIds.toMutableSet().apply {
                        if (isSavedMode) {
                            // Nếu đang ở màn hình từ đã lưu và nhấn bỏ lưu, xóa ID này khỏi danh sách chọn
                            remove(targetWord.id)
                        }
                    }

                    currentState.copy(
                        vocabWords = currentState.vocabWords.copy(data = updatedList),
                        selectedWordIds = updatedSelectedIds // Gán danh sách ID mới vào state
                    )
                }

                viewModelScope.launch {
                    toggleSaveWordUseCase(targetWord.id)
                        .onError {
                            if (isSavedMode) loadSavedWords() else topicId?.let { loadTopicWords(it) }
                        }
                }
            }

            is VocabularyListEvent.OnFlashCardClick -> {
                val allWords = _state.value.vocabWords.data ?: emptyList()
                val selectedWords = allWords.filter { _state.value.selectedWordIds.contains(it.id) }

                if (selectedWords.isNotEmpty()) {
                    flashcardDataStore.words = selectedWords
                    sendEffect(VocabularyListEffect.NavigateToFlashCard)
                } else {
                    sendGlobalEffect(BaseEffect.ShowToast(UiText.DynamicString("Please select at least one word to practice!")))
                }
            }

            is VocabularyListEvent.OnPlayAudio -> {
                playAudio(event.word.usAudioUrl, event.word.word)
            }
        }
    }

    private fun playAudio(url: String?, word: String) {
        textToSpeechParser.playAudio(
            url = url,
            textToSpeak = word,
            speed =  1.0f,
            scope = viewModelScope
        )
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeechParser.release()
    }
}