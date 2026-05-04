package com.codework.dailyenglish.presentation.screen.lessons.lesson

import android.media.MediaPlayer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.lesson.model.QuestionContent
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.domain.lesson.usecase.CompleteLessonUseCase
import com.codework.dailyenglish.domain.lesson.usecase.GetLessonPlayDataUseCase
import com.codework.dailyenglish.domain.lesson.usecase.UpdateLessonProgressUseCase
import com.codework.dailyenglish.presentation.manager.TextToSpeechManager
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonConfigUi
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEffect
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEvent
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val getLessonPlayDataUseCase: GetLessonPlayDataUseCase,
    private val updateLessonProgressUseCase: UpdateLessonProgressUseCase,
    private val completeLessonUseCase: CompleteLessonUseCase,
    private val ttsManager: TextToSpeechManager,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<LessonState, LessonEvent, LessonEffect>(
    initialState = BaseUiState(isLoading = true)
) {
    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])
    private var mediaPlayer: MediaPlayer? = null

    init {
        loadLessonData()
    }


    override fun onEvent(event: LessonEvent) {
        when (event) {
            is LessonEvent.OnNextStep -> nextStep()
            is LessonEvent.OnPreviousStep -> previousStep()
            is LessonEvent.OnAnswerInput -> updateUserAnswer(event.answer, event.indices)
            is LessonEvent.OnCheckAnswerClicked -> checkAnswer()
            is LessonEvent.OnPlayAudio -> playAudio(event.url, event.word, event.speed)
            is LessonEvent.OnQuitClick -> updateConfig {
                it.copy(isQuitDialogVisible = true)
            }
            is LessonEvent.OnDismissQuitDialog -> updateConfig {
                it.copy(isQuitDialogVisible = false)
            }
            is LessonEvent.OnConfirmQuit -> {
                viewModelScope.launch {
                    updateConfig { it.copy(isQuitDialogVisible = false) }
                    delay(100)
                    sendEffect(LessonEffect.NavigateBack)
                }
            }
            else -> {}
        }
    }

    private fun checkAnswer() {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val currentStep = currentData.steps[currentData.currentIndex]
            val questionData = currentStep.data as? StepData.Question ?: return@update state

            val qData = questionData
            val oId = qData.selectedOptionId ?: qData.userAnswer

            // 1. CHẤM ĐIỂM NGAY LÚC NÀY (Đưa logic từ nextStep lên đây)
            var isCorrect = false
            var correctAnswerText: String? = null // Lưu đáp án đúng dưới dạng Text

            when (val content = qData.content) {
                is QuestionContent.MultipleChoice -> {
                    isCorrect = content.options.find { it.id == oId }?.isCorrect == true
                    // Lấy text của option đúng để hiện
                    correctAnswerText = content.options.find { it.isCorrect }?.text
                }
                is QuestionContent.SentenceBuilder -> {
                    val userSentence = oId.trim().replace(Regex("\\s+"), " ")
                    val correctSentence = content.correctSentence.trim().replace(Regex("\\s+"), " ")
                    isCorrect = userSentence.equals(correctSentence, ignoreCase = true)
                    correctAnswerText = content.correctSentence // Hiện cả câu đúng
                }
                is QuestionContent.FillBlank -> {
                    isCorrect = oId.trim().equals(content.correctAnswer.trim(), ignoreCase = true)
                    correctAnswerText = content.correctAnswer
                }
                is QuestionContent.Dictation -> {
                    val userAnswer = oId.trim()
                    isCorrect = userAnswer.equals(content.targetWord.trim(), ignoreCase = true) ||
                            userAnswer.equals(content.correctAnswer.trim(), ignoreCase = true) ||
                            content.acceptedAnswers.any { it.trim().equals(userAnswer, ignoreCase = true) }
                    correctAnswerText = content.correctAnswer
                }
                is QuestionContent.Speaking -> {
                    isCorrect = oId.trim().equals(content.targetText.trim(), ignoreCase = true) || oId == "PASSED"
                    correctAnswerText = content.targetText
                }
                is QuestionContent.Matching -> {
                    isCorrect = oId == "CORRECT" || oId == "PASSED"
                }
                else -> {
                    isCorrect = false
                }
            }

            // 2. CẬP NHẬT STATE: Báo là đã chấm + Kết quả đúng/sai + Text đáp án
            val updatedQuestionData = questionData.copy(
                isAnswerEvaluated = true,
                isCorrect = isCorrect
                // Ghi chú: Nếu trong StepData.Question của bạn có thêm trường `correctAnswerText`,
                // bạn có thể truyền thẳng correctAnswerText vào đây.
                // Nếu chưa có, bạn có thể truyền qua UI theo cách ở Bước 2.
            )

            val updatedSteps = currentData.steps.toMutableList()
            updatedSteps[currentData.currentIndex] = currentStep.copy(data = updatedQuestionData)
            state.copy(data = currentData.copy(
                steps = updatedSteps,
                showResultStepIndex = currentData.currentIndex
            ))
        }
    }

    private fun updateUserAnswer(answer: String, indices: List<Int>) {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val currentStep = currentData.steps[currentData.currentIndex]
            val questionData = currentStep.data as? StepData.Question ?: return@update state

            val updatedQuestionData = questionData.copy(
                userAnswer = answer,
                selectedOptionId = answer.ifEmpty { null },
                selectedIndices = indices
            )

            val updatedSteps = currentData.steps.toMutableList()
            updatedSteps[currentData.currentIndex] = currentStep.copy(data = updatedQuestionData)
            state.copy(data = currentData.copy(steps = updatedSteps))
        }
    }

    private fun updateConfig(updateBlock: (LessonConfigUi) -> LessonConfigUi) {
        _state.update { currentState ->
            val currentData = currentState.data
            if (currentData != null) {
                currentState.copy(data = updateBlock(currentData))
            } else {
                currentState
            }
        }
    }

    fun playAudio(url: String?, textToSpeak: String? = null, speed: Float = 1f) {
        if (url.isNullOrBlank()) {
            textToSpeak?.let { ttsManager.speak(it, speed) }
            return
        }
        viewModelScope.launch {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                } else {
                    mediaPlayer?.reset()
                }

                mediaPlayer?.apply {
                    setDataSource(url)
                    setOnPreparedListener { mp ->
                        try {
                            mp.playbackParams = mp.playbackParams.setSpeed(speed)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        mp.start()
                    }
                    setOnErrorListener { _, _, _ ->
                        textToSpeak?.let { ttsManager.speak(it, speed) }
                        true
                    }
                    prepareAsync()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                textToSpeak?.let { ttsManager.speak(it, speed) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
        ttsManager.shutDown()
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

    private fun submitLesson() {
        viewModelScope.launch {
            _state.update { currentState ->
                val currentData = currentState.data
                currentState.copy(
                    isLoading = true,
                    data = currentData?.copy(showResultStepIndex = null)
                )
            }

            completeLessonUseCase(lessonId)
                .onSuccess { data ->
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(
                        LessonEffect.NavigateToResult(
                            scorePercentage = data.scorePercentage,
                            starsEarned = data.starsEarned,
                            lessonId = lessonId
                        )
                    )
                }
                .onError { err ->
                    _state.update { it.copy(isLoading = false, error = err.asUiText()) }
                }
        }
    }



    private fun optionSelected(optionId: String) {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val currentStep = currentData.steps[currentData.currentIndex]
            val questionData = currentStep.data as? StepData.Question ?: return@update state
            if (questionData.isAnswerEvaluated) return@update state
            val shouldCheckImmediately = questionData.content is QuestionContent.MultipleChoice
            val updatedQuestionData = questionData.copy(
                selectedOptionId = optionId,
                isAnswerEvaluated = shouldCheckImmediately
            )
            val updatedSteps = currentData.steps.toMutableList()
            updatedSteps[currentData.currentIndex] = currentStep.copy(data = updatedQuestionData)
            state.copy(data = currentData.copy(steps = updatedSteps))
        }
    }



    private fun nextStep() {
        val currentData = _state.value.data ?: return
        val currentIndex = currentData.currentIndex
        val currentStep = currentData.steps[currentIndex]
        val nextIndex = currentIndex + 1

        var qId: String? = null
        var oId: String? = null
        var uAnswer: String? = null
        var sIndices: List<Int>? = null
        var isCorrect: Boolean? = null

        if (currentStep.data is StepData.Question) {
            val qData = currentStep.data
            qId = currentStep.refId

            oId = qData.selectedOptionId

            uAnswer = qData.userAnswer

            sIndices = qData.selectedIndices

            isCorrect = qData.isCorrect
        }

        viewModelScope.launch(Dispatchers.IO) {
            updateLessonProgressUseCase(
                lessonId = lessonId,
                cursor = if (nextIndex < currentData.steps.size) nextIndex else currentIndex,
                questionId = qId,
                optionId = oId,
                userAnswer = uAnswer,
                selectedIndices = sIndices,
                isCorrect = isCorrect
            )
        }
        _state.update {
            it.copy(data = currentData.copy(showResultStepIndex = null))
        }
        if (nextIndex < currentData.steps.size) {
            _state.update {
                it.copy(data = it.data?.copy(currentIndex = nextIndex))
            }

            val newlyRevealedStep = currentData.steps[nextIndex]
            if (newlyRevealedStep.data is StepData.Vocab) {
                playAudio(newlyRevealedStep.data.usAudioUrl, newlyRevealedStep.data.word, 1f)
            }
        } else {
            submitLesson()
        }
    }

    private fun previousStep() {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val prevIndex = currentData.currentIndex - 1

            if (prevIndex >= 0) {
                val newData = currentData.copy(currentIndex = prevIndex)
                trackProgress(prevIndex)
                state.copy(data = newData)
            } else {
                state.copy(data = currentData.copy(isQuitDialogVisible = true))
            }
        }
    }

    private fun trackProgress(cursorIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            updateLessonProgressUseCase(lessonId, cursorIndex)
        }
    }




}