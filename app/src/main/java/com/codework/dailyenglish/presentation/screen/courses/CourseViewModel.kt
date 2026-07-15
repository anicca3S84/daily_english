package com.codework.dailyenglish.presentation.screen.courses

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.base.BaseEffect.ShowToast
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.course.usecase.GetDashboardDataUseCase
import com.codework.dailyenglish.domain.user.usecase.GetUserInfoUseCase // BỔ SUNG
import com.codework.dailyenglish.domain.auth.usecase.GetMyProfileUseCase // BỔ SUNG (Đồng bộ ngầm)
import com.codework.dailyenglish.presentation.components.AppAdManager
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
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase
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
                        _state.update { it.copy(showSubscriptionPopup = true) }
                    } else {
                    sendEffect(NavigateToCourseDetails(event.courseId, event.courseCode))

                }
            }

            CourseEvent.OnRefreshData -> loadDashboardData()

            CourseEvent.OnDismissSubscriptionPopup -> {
                _state.update { it.copy(showSubscriptionPopup = false) }
                loadDashboardData() // Refresh lại danh sách để mở khóa lập tức nếu vừa mua VIP thành công
            }


            is CourseEvent.OnRefreshData -> loadDashboardData()

            is CourseEvent.OnDismissSubscriptionPopup -> {
                _state.update { it.copy(showSubscriptionPopup = false) }
                loadDashboardData()
            }
        }
    }

    private fun loadDashboardData() {
        _state.update { it.copy(dashboardState = it.dashboardState.copy(isLoading = true, error = null)) }

        viewModelScope.launch {
            val localUser = getUserInfoUseCase()
            val isPremiumLocal = localUser?.isPremium ?: false

            _state.update { it.copy(isPremium = isPremiumLocal) }
            AppAdManager.isPremiumUser = isPremiumLocal

            launch {
                getMyProfileUseCase().onSuccess { freshUser ->
                    _state.update { it.copy(isPremium = freshUser.isPremium) }
                    AppAdManager.isPremiumUser = freshUser.isPremium
                }
            }

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