package com.codework.dailyenglish.presentation.screen.lessons.lesson

import android.content.Context
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
import com.codework.dailyenglish.domain.lesson.usecase.ReviewLessonUseCase
import com.codework.dailyenglish.domain.lesson.usecase.UpdateLessonProgressUseCase
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonConfigUi
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEffect
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEvent
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonState
import com.codework.dailyenglish.presentation.utils.asUiText
import com.codework.dailyenglish.presentation.utils.calculateSimilarity
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.utils.TextToSpeechParser
import com.codework.dailyenglish.core.utils.VoiceToTextParser
import com.codework.dailyenglish.domain.dictionary.usecase.RestartLessonUseCase
import com.codework.dailyenglish.domain.dictionary.usecase.ToggleSaveWordUseCase
import com.codework.dailyenglish.notification.ReminderManager
import com.codework.dailyenglish.presentation.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val restartLessonUseCase: RestartLessonUseCase,
    private val reviewLessonUseCase: ReviewLessonUseCase,
    private val getLessonPlayDataUseCase: GetLessonPlayDataUseCase,
    private val updateLessonProgressUseCase: UpdateLessonProgressUseCase,
    private val completeLessonUseCase: CompleteLessonUseCase,
    private val textToSpeechParser: TextToSpeechParser,
    private val speachToTextParser: VoiceToTextParser,
    private val toggleSaveWordUseCase: ToggleSaveWordUseCase,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<LessonState, LessonEvent, LessonEffect>(
    initialState = BaseUiState(isLoading = true)
) {
    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])
    private val isCompleted: Boolean = checkNotNull(savedStateHandle["isCompleted"])
    private val isReview: Boolean = savedStateHandle["isReview"] ?: false

    private val reminderManager = ReminderManager(context)

    init {
        loadLessonData()
        observeSpeechToText()
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
            is LessonEvent.OnToggleSaveWord -> {
                viewModelScope.launch {
                    event.wordId.let {
                        toggleSaveWordUseCase(event.wordId)
                            .onSuccess {
                                sendGlobalEffect(
                                    BaseEffect.ShowToast(UiText.StringResource(R.string.noti_success))
                                )
                            }
                            .onError { err ->
                                val uiError = err.asUiText()
                                sendGlobalEffect(BaseEffect.ShowToast(uiError))
                            }
                    }
                }
            }
            is LessonEvent.OnStartSpeech -> speachToTextParser.startListening(event.context)
            is LessonEvent.OnStopSpeech -> speachToTextParser.stopListening()
            else -> {}
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    private fun checkAnswer() {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val currentStep = currentData.steps[currentData.currentIndex]
            val questionData = currentStep.data as? StepData.Question ?: return@update state
            var isCorrect: Boolean

            when (val content = questionData.content) {
                is QuestionContent.MultipleChoice -> {
                    val selectedId = questionData.selectedOptionId
                    isCorrect = content.options.find { it.id == selectedId }?.isCorrect == true
                }
                is QuestionContent.SentenceBuilder -> {
                    val userSentence = questionData.userAnswer.trim().replace(Regex("\\s+"), " ")
                    val correctSentence = content.correctSentence.trim().replace(Regex("\\s+"), " ")
                    isCorrect = userSentence.equals(correctSentence, ignoreCase = true)
                }
                is QuestionContent.FillBlank -> {
                    val answerText = questionData.userAnswer.trim()
                    isCorrect = answerText.equals(content.correctAnswer.trim(), ignoreCase = true)
                }
                is QuestionContent.Speaking -> {
                    speachToTextParser.stopListening()
                    val similarity = (questionData.userAnswer ?: "").calculateSimilarity(content.targetText)
                    isCorrect = similarity >= 0.8f || questionData.userAnswer == "PASSED"
                }
                else -> {
                    isCorrect = false
                }
            }

            val updatedQuestionData = questionData.copy(
                isAnswerEvaluated = true,
                isCorrect = isCorrect
            )

            val updatedSteps = currentData.steps.toMutableList()
            updatedSteps[currentData.currentIndex] = currentStep.copy(data = updatedQuestionData)
            state.copy(data = currentData.copy(
                steps = updatedSteps,
                showResultStepIndex = currentData.currentIndex
            ))
        }
    }

    private fun updateUserAnswer(
        answer: String,
        indices: List<Int>,
        isFinalSpeaking: Boolean = false // Thêm tham số này với giá trị mặc định là false
    ) {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val currentStep = currentData.steps[currentData.currentIndex]
            val questionData = currentStep.data as? StepData.Question ?: return@update state

            val isMultipleChoice = questionData.content is QuestionContent.MultipleChoice
            val isSpeaking = questionData.content is QuestionContent.Speaking

            var updatedQuestionData = questionData.copy(
                selectedOptionId = if (isMultipleChoice) answer.ifEmpty { null } else null,
                userAnswer = if (!isMultipleChoice) answer else "",
                selectedIndices = indices
            )

            if (isSpeaking && answer.isNotBlank()) {
                val content = questionData.content
                val similarity = answer.calculateSimilarity(content.targetText)
                val isCorrectAnswer = similarity >= 0.8f || answer == "PASSED"

                if (isCorrectAnswer) {
                    updatedQuestionData = updatedQuestionData.copy(
                        isAnswerEvaluated = true,
                        isCorrect = true
                    )
                    speachToTextParser.stopListening()
                } else if (isFinalSpeaking) {
                    updatedQuestionData = updatedQuestionData.copy(
                        isAnswerEvaluated = true,
                        isCorrect = false
                    )
                } else {
                    updatedQuestionData = updatedQuestionData.copy(
                        isAnswerEvaluated = false,
                        isCorrect = null
                    )
                }
            }

            val updatedSteps = currentData.steps.toMutableList()
            updatedSteps[currentData.currentIndex] = currentStep.copy(data = updatedQuestionData)

            state.copy(data = currentData.copy(
                steps = updatedSteps,
                showResultStepIndex = if (isSpeaking && updatedQuestionData.isAnswerEvaluated) currentData.currentIndex else currentData.showResultStepIndex
            ))
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

    private fun loadLessonData() {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = when {
                isCompleted && isReview -> {
                    reviewLessonUseCase(lessonId)
                }
                isCompleted && !isReview -> {
                    restartLessonUseCase(lessonId)
                }
                else -> {
                    getLessonPlayDataUseCase(lessonId)
                }
            }

            result.onSuccess { playData ->
                _state.update { it.copy(
                    isLoading = false,
                    data = LessonConfigUi(
                        lessonId = playData.lessonId,
                        steps = playData.steps,
                        currentIndex = playData.resumeCursor,
                    )
                )}
                val stepData = playData.steps.getOrNull(playData.resumeCursor)?.data
                if (stepData is StepData.Vocab) {
                    playAudio(stepData.usAudioUrl, stepData.word, 1f)
                }
            }
                .onError { err ->
                    _state.update { it.copy(isLoading = false, error = err.asUiText()) }
                }
        }
    }

    private suspend fun submitLesson() {
        if (!isNetworkAvailable()) {
            sendGlobalEffect(BaseEffect.ShowToast(UiText.StringResource(R.string.error_network)))
            return
        }

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

                reminderManager.rescheduleDailyReminders()

                sendEffect(
                    LessonEffect.NavigateToResult(
                        scorePercentage = data.scorePercentage,
                        starsEarned = data.starsEarned,
                        lessonId = lessonId,
                        streak = data.streak
                    )
                )
            }
            .onError { err ->
                _state.update { it.copy(isLoading = false, error = err.asUiText()) }
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

        viewModelScope.launch {
            updateLessonProgressUseCase(
                lessonId = lessonId,
                cursor = if (nextIndex < currentData.steps.size) nextIndex else currentIndex,
                questionId = qId,
                optionId = oId,
                userAnswer = uAnswer,
                selectedIndices = sIndices,
                isCorrect = isCorrect
            )

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
                if (isReview) {
                    sendEffect(LessonEffect.NavigateBack)
                } else {
                    submitLesson()
                }
            }
        }
    }

    private fun previousStep() {
        _state.update { state ->
            val currentData = state.data ?: return@update state
            val prevIndex = currentData.currentIndex - 1

            if (prevIndex >= 0) {
                val newData = currentData.copy(
                    currentIndex = prevIndex,
                    showResultStepIndex = null
                )
                trackProgress(prevIndex)
                state.copy(data = newData)
            } else {
                if (isReview) {
                    viewModelScope.launch {
                        sendEffect(LessonEffect.NavigateBack)
                    }
                    state
                } else {
                    state.copy(data = currentData.copy(isQuitDialogVisible = true))
                }
            }
        }
    }

    private fun trackProgress(cursorIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            updateLessonProgressUseCase(lessonId, cursorIndex)
        }
    }

    private fun playAudio(url: String?, word: String, speed: Float = 1f) {
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

    private fun observeSpeechToText() {
        viewModelScope.launch {
            speachToTextParser.state.collect { voiceState ->
                _state.update { currentState ->
                    val currentData = currentState.data
                    if (currentData != null) {
                        currentState.copy(
                            data = currentData.copy(voiceState = voiceState)
                        )
                    } else {
                        currentState
                    }
                }

                if (voiceState.spokenText.isNotBlank()) {
                    updateUserAnswer(
                        answer = voiceState.spokenText,
                        indices = emptyList(),
                        isFinalSpeaking = !voiceState.isListening
                    )
                }
            }
        }
    }
}