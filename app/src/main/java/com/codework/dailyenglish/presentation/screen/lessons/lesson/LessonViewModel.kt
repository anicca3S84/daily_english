package com.codework.dailyenglish.presentation.screen.lessons.lesson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.domain.lesson.usecase.GetLessonPlayDataUseCase
import com.codework.dailyenglish.domain.lesson.usecase.UpdateLessonProgressUseCase
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonConfigUi
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEffect
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEvent
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val getLessonPlayDataUseCase: GetLessonPlayDataUseCase,
    private val updateLessonProgressUseCase: UpdateLessonProgressUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<LessonState, LessonEvent, LessonEffect>(
    initialState = BaseUiState(isLoading = true)
) {
    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])

    init {
        loadLessonData()
    }

    override fun onEvent(event: LessonEvent) {
        when (event) {
            LessonEvent.OnNextStep -> handleNextStep()
            LessonEvent.OnPreviousStep -> handlePreviousStep()
            is LessonEvent.OnOptionSelected -> handleOptionSelected(event.optionId)
            LessonEvent.OnCheckAnswerClicked -> handleCheckAnswer()
            else -> {}
        }
    }

    private fun loadLessonData() {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            getLessonPlayDataUseCase(lessonId)
                .onSuccess { playData ->
                    _state.update { it.copy(
                        isLoading = false,
                        data = LessonConfigUi(
                            lessonId = playData.lessonId,
                            steps = playData.steps,
                            currentIndex = playData.resumeCursor
                        )
                    )}
                }
                .onError { err ->
                    _state.update { it.copy(isLoading = false, error = err.asUiText()) }
                }
        }
    }

    private fun handleNextStep() {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val currentStep = currentData.currentStep
            val nextIndex = currentData.currentIndex + 1

            var qId: String? = null
            var oId: String? = null
            var isCorrect: Boolean? = null

            if (currentStep?.data is StepData.Question) {
                val qData = currentStep.data
                qId = currentStep.refId
                oId = qData.selectedOptionId
                isCorrect = qData.options.find { it.id == oId }?.isCorrect ?: false
            }

            viewModelScope.launch(Dispatchers.IO) {
                updateLessonProgressUseCase(
                    lessonId = lessonId,
                    cursor = nextIndex,
                    questionId = qId,
                    optionId = oId,
                    isCorrect = isCorrect
                )
            }
            if (nextIndex < currentData.steps.size) {
                state.copy(data = currentData.copy(currentIndex = nextIndex))
            } else {
                sendEffect(LessonEffect.NavigateToResult(lessonId))
                state
            }
        }
    }

    private fun handlePreviousStep() {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val prevIndex = currentData.currentIndex - 1

            if (prevIndex >= 0) {
                val newData = currentData.copy(currentIndex = prevIndex)
                trackProgress(prevIndex)
                state.copy(data = newData)
            } else {
                sendEffect(LessonEffect.ShowQuitDialog(0f))
                state
            }
        }
    }

    private fun handleOptionSelected(optionId: String) {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val currentStep = currentData.steps[currentData.currentIndex]
            val questionData = currentStep.data as? StepData.Question ?: return@update state
            if (questionData.isChecked) return@update state
            val updatedQuestionData = questionData.copy(
                selectedOptionId = optionId,
                isChecked = true
            )
            val updatedStep = currentStep.copy(data = updatedQuestionData)
            val updatedSteps = currentData.steps.toMutableList()
            updatedSteps[currentData.currentIndex] = updatedStep
            state.copy(data = currentData.copy(steps = updatedSteps))
        }
    }

    private fun handleCheckAnswer() {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val currentStep = currentData.steps[currentData.currentIndex]
            val questionData = currentStep.data as? StepData.Question ?: return@update state

            if (!questionData.isChecked) {
                val updatedStep = currentStep.copy(data = questionData.copy(isChecked = true))
                val updatedSteps = currentData.steps.toMutableList()
                updatedSteps[currentData.currentIndex] = updatedStep
                state.copy(data = currentData.copy(steps = updatedSteps))
            } else {
                state
            }
        }

        val currentData = _state.value.data ?: return
        val stepData = currentData.steps[currentData.currentIndex].data
        if (stepData is StepData.Question && stepData.isChecked) {
            handleNextStep()
        }
    }

    private fun trackProgress(cursorIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            updateLessonProgressUseCase(lessonId, cursorIndex)
        }
    }
}