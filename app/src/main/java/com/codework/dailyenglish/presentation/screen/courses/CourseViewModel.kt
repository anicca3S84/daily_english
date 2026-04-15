package com.codework.dailyenglish.presentation.screen.courses

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.base.BaseEffect.ShowToast
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.course.usecase.GetDashboardDataUseCase
import com.codework.dailyenglish.presentation.screen.courses.mapper.toUiModels
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseEffect
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseEffect.*
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseEvent
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseState
import com.codework.dailyenglish.presentation.utils.UiText
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase
) : BaseViewModel<CourseState, CourseEvent, CourseEffect>(
    initialState = CourseState()
) {

    init {
        loadDashboardData()
    }

    override fun onEvent(event: CourseEvent) {
        when (event) {
            is CourseEvent.OnCourseClick -> {
                if (event.isLocked) {
                    sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_course_looked)))
                } else {
                    sendEffect(NavigateToCourseDetails(event.courseId, event.courseCode))
                }
            }
            CourseEvent.OnRefreshData -> loadDashboardData()
        }
    }

    private fun loadDashboardData() {
        _state.update { it.copy(dashboardState = it.dashboardState.copy(isLoading = true, error = null)) }

        viewModelScope.launch {
            getDashboardDataUseCase()
                .onSuccess { result ->
                    _state.update { state ->
                        state.copy(
                            dashboardState = state.dashboardState.copy(
                                data = result.courses.toUiModels(),
                                isLoading = false
                            ),
                            streakValue = result.stats.streak,
                            expValue = result.stats.totalExp,
                            lessonsDone = result.stats.lessonsDone,
                            wordsLearned = result.stats.wordsLearned
                        )
                    }
                }
                .onError { appError ->
                    _state.update {
                        it.copy(
                            dashboardState = it.dashboardState.copy(
                                isLoading = false,
                                error = appError.asUiText()
                            )
                        )
                    }
                }
        }
    }
}