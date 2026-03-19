package com.codework.daily_english.presentation.screen.dictionary

import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.core.base.BaseState
import com.codework.daily_english.presentation.screen.dictionary.model.Word
import com.codework.daily_english.presentation.screen.dictionary.mvi.DictionaryEffect
import com.codework.daily_english.presentation.screen.dictionary.mvi.DictionaryEvent
import com.codework.daily_english.presentation.screen.dictionary.mvi.DictionaryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor() : BaseViewModel<DictionaryState, DictionaryEvent, DictionaryEffect>() {

    private val mockDictionary = listOf(
        Word(
            word = "apple",
            phoneticUk = "/ˈæp.əl/",
            phoneticUs = "/ˈæp.əl/",
            type = "noun",
            definition = "A round fruit with red or green skin and a white inside.",
            example = "She ate a fresh apple."
        ),
        Word(
            word = "application",
            phoneticUk = "/ˌæp.lɪˈkeɪ.ʃən/",
            phoneticUs = "/ˌæp.ləˈkeɪ.ʃən/",
            type = "noun",
            definition = "A formal request to be considered for a position.",
            example = "I submitted my job application."
        )
    )

    init {
        // Khởi tạo data ban đầu luôn là Success (vì Dashboard không cần call API load lâu)
        _uiState.update { BaseState.Success(DictionaryState()) }
    }

    override fun onEvent(event: DictionaryEvent) {
        val currentState = (_uiState.value as? BaseState.Success)?.data ?: return

        when (event) {
            is DictionaryEvent.OnSearchQueryChange -> {
                val query = event.query
                val results = if (query.isBlank()) {
                    emptyList()
                } else {
                    mockDictionary.filter { it.word.contains(query, ignoreCase = true) }
                }

                _uiState.update {
                    BaseState.Success(currentState.copy(query = query, searchResults = results))
                }
            }

            is DictionaryEvent.OnClearSearch -> {
                _uiState.update {
                    BaseState.Success(currentState.copy(query = "", searchResults = emptyList()))
                }
            }

            is DictionaryEvent.OnPlayAudio -> {
                // Đẩy Effect ra UI để UI gọi TextToSpeech hoặc ExoPlayer
                sendEffect(DictionaryEffect.PlayAudio(event.word, event.accent))
            }

            is DictionaryEvent.OnToggleSaveWord -> {
                // Logic đổi trạng thái isSaved (Giả lập)
                val updatedResults = currentState.searchResults.map {
                    if (it.word == event.word.word) it.copy(isSaved = !it.isSaved) else it
                }
                _uiState.update { BaseState.Success(currentState.copy(searchResults = updatedResults)) }

                val status = if (!event.word.isSaved) "Saved" else "Removed"
                sendEffect(DictionaryEffect.ShowToast("$status ${event.word.word}"))
            }

            is DictionaryEvent.OnQuickActionClick -> sendEffect(DictionaryEffect.NavigateToFeature(event.action))
            is DictionaryEvent.OnTopicClick -> sendEffect(DictionaryEffect.ShowToast("Exploring topic: ${event.topic}"))
            is DictionaryEvent.OnWordClick -> { /* Navigate to detail screen if needed */ }
        }
    }
}