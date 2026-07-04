package com.codework.dailyenglish.presentation.screen.lessons.preview

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.lesson_preview.usecase.GetLessonPreviewUseCase
import com.codework.dailyenglish.presentation.screen.lessons.preview.mvi.LessonPreviewEffect
import com.codework.dailyenglish.presentation.screen.lessons.preview.mvi.LessonPreviewEvent
import com.codework.dailyenglish.presentation.screen.lessons.preview.mvi.LessonPreviewState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonPreviewViewModel @Inject constructor(
    private val getLessonPreviewUseCase: GetLessonPreviewUseCase,
) : BaseViewModel<LessonPreviewState, LessonPreviewEvent, LessonPreviewEffect>(
    initialState = LessonPreviewState()
) {

    override fun onEvent(event: LessonPreviewEvent) {
        when (event) {
            is LessonPreviewEvent.OnLoadPreview -> {
                loadLessonPreview(event.lessonId)
            }

            is LessonPreviewEvent.OnStartLearningClick -> {
                sendEffect(LessonPreviewEffect.NavigateToLearningSession(event.lessonId, isReview = false))
            }
            is LessonPreviewEvent.OnReviewClick -> {
                sendEffect(LessonPreviewEffect.NavigateToLearningSession(event.lessonId, isReview = true))
            }
            is LessonPreviewEvent.OnRestartClick -> {
                sendEffect(LessonPreviewEffect.NavigateToLearningSession(event.lessonId, isReview = false))
            }


            LessonPreviewEvent.OnCloseClick -> {
                sendEffect(LessonPreviewEffect.CloseBottomSheet)
            }
            LessonPreviewEvent.OnRefresh -> {
                state.value.previewDataState.data?.id?.let { loadLessonPreview(it) }
            }
            else -> {}
        }
    }

    private fun loadLessonPreview(id: String) {
        if (id.isBlank()) return

        _state.update {
            it.copy(previewDataState = it.previewDataState.copy(isLoading = true, error = null))
        }

        viewModelScope.launch {
            getLessonPreviewUseCase(id)
                .onSuccess { result ->
                    _state.update { state ->
                        state.copy(
                            previewDataState = state.previewDataState.copy(
                                isLoading = false,
                                data = result
                            )
                        )
                    }
                }
                .onError { appError ->
                    _state.update { state ->
                        state.copy(
                            previewDataState = state.previewDataState.copy(
                                isLoading = false,
                                error = appError.asUiText()
                            )
                        )
                    }
                }
        }
    }
}