package com.codework.dailyenglish.presentation.screen.lesson_preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.lesson_preview.usecase.GetLessonPreviewUseCase
import com.codework.dailyenglish.presentation.screen.lesson_preview.mvi.LessonPreviewEffect
import com.codework.dailyenglish.presentation.screen.lesson_preview.mvi.LessonPreviewEvent
import com.codework.dailyenglish.presentation.screen.lesson_preview.mvi.LessonPreviewState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonPreviewViewModel @Inject constructor(
    private val getLessonPreviewUseCase: GetLessonPreviewUseCase,
    // savedStateHandle: SavedStateHandle // Có thể bỏ nếu không dùng Navigation để mở màn hình này
) : BaseViewModel<LessonPreviewState, LessonPreviewEvent, LessonPreviewEffect>(
    initialState = LessonPreviewState()
) {
    // XÓA BLOCK init { loadLessonPreview() }

    override fun onEvent(event: LessonPreviewEvent) {
        when (event) {
            // Thêm sự kiện load dữ liệu có kèm ID từ UI gửi xuống
            is LessonPreviewEvent.OnLoadPreview -> {
                loadLessonPreview(event.lessonId)
            }

            is LessonPreviewEvent.OnStartLearningClick -> {
                sendEffect(LessonPreviewEffect.NavigateToLearningSession(event.lessonId))
            }
            LessonPreviewEvent.OnCloseClick -> {
                sendEffect(LessonPreviewEffect.CloseBottomSheet)
            }
            // Nếu bạn có nút Refresh trên UI
            LessonPreviewEvent.OnRefresh -> {
                // Bạn có thể lưu lại lessonId trong state để dùng ở đây
                state.value.previewDataState.data?.id?.let { loadLessonPreview(it) }
            }
            else -> {}
        }
    }

    private fun loadLessonPreview(id: String) {
        if (id.isBlank()) return // Chặn ngay nếu ID trống

        _state.update {
            it.copy(previewDataState = it.previewDataState.copy(isLoading = true, error = null))
        }

        viewModelScope.launch {
            getLessonPreviewUseCase(id) // Dùng ID truyền vào thay vì lessonId từ SavedStateHandle
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