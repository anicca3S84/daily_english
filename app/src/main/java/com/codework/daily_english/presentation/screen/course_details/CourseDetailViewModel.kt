package com.codework.daily_english.presentation.screen.course_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.R
import com.codework.daily_english.core.base.BaseEffect.ShowToast
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.core.network.onError
import com.codework.daily_english.core.network.onSuccess
import com.codework.daily_english.domain.course_detail.usecase.GetCourseDetailUseCase
import com.codework.daily_english.presentation.screen.course_details.mapper.toUiModels
import com.codework.daily_english.presentation.screen.course_details.mvi.CourseDetailEffect
import com.codework.daily_english.presentation.screen.course_details.mvi.CourseDetailEvent
import com.codework.daily_english.presentation.screen.course_details.mvi.CourseDetailState
import com.codework.daily_english.presentation.theme.provider.CourseThemeProvider
import com.codework.daily_english.presentation.utils.UiText
import com.codework.daily_english.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val getCourseDetailUseCase: GetCourseDetailUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CourseDetailState, CourseDetailEvent, CourseDetailEffect>(
    initialState = CourseDetailState(
        courseTheme = CourseThemeProvider.getThemeByCode(
            savedStateHandle.get<String>("courseCode")
        )
    )
) {
    private val courseId: String = savedStateHandle.get<String>("courseId") ?: ""

    init {
        loadCourseDetail()
    }

    override fun onEvent(event: CourseDetailEvent) {
        when (event) {
            is CourseDetailEvent.OnLessonClick -> {
                if (event.isLocked) {
                    sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_lesson_looked)))
                } else {
                    sendEffect(CourseDetailEffect.NavigateToLearning(event.lessonId))
                }
            }
            CourseDetailEvent.OnRefresh -> {
                loadCourseDetail()
            }
        }
    }

    private fun loadCourseDetail() {
        _state.update { it.copy(modulesState = it.modulesState.copy(isLoading = true, error = null)) }

        viewModelScope.launch {
            getCourseDetailUseCase(courseId)
                .onSuccess { result ->
                    _state.update { state ->
                        state.copy(
                            courseTitle = result.courseTitle,
                            overallProgress = result.overallProgress,
                            totalLessons = result.totalLessons,
                            completedLessons = result.completedLessons,
                            modulesState = state.modulesState.copy(
                                isLoading = false,
                                data = result.modules.toUiModels()
                            )
                        )
                    }
                }
                .onError { appError ->
                    _state.update {
                        it.copy(
                            modulesState = it.modulesState.copy(
                                isLoading = false,
                                error = appError.asUiText()
                            )
                        )


                    }
                }
        }
    }
}