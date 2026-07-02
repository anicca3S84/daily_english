package com.codework.dailyenglish.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.codework.dailyenglish.presentation.components.AppCongratulationsScreen
import com.codework.dailyenglish.presentation.screen.ai.*
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEffect
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEvent
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiState
import com.codework.dailyenglish.presentation.screen.ai_speaking.AiSpeakingScreen
import com.codework.dailyenglish.presentation.screen.ai_speaking.AiSpeakingViewModel
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEffect
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEvent
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingState
import com.codework.dailyenglish.presentation.screen.auth.login.LoginScreen
import com.codework.dailyenglish.presentation.screen.auth.login.LoginViewModel
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginEffect
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginEvent
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginState
import com.codework.dailyenglish.presentation.screen.course_details.CourseDetailViewModel
import com.codework.dailyenglish.presentation.screen.course_details.CourseDetailsScreen
import com.codework.dailyenglish.presentation.screen.courses.*
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseEffect
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseEvent
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseState
import com.codework.dailyenglish.presentation.screen.dictionary.*
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryEffect
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryEvent
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryState
import com.codework.dailyenglish.presentation.screen.grammar.*
import com.codework.dailyenglish.presentation.screen.grammar.mvi.GrammarEffect
import com.codework.dailyenglish.presentation.screen.grammar.mvi.GrammarEvent
import com.codework.dailyenglish.presentation.screen.grammar.mvi.GrammarState
import com.codework.dailyenglish.presentation.screen.home.*
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEffect
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEvent
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeState
import com.codework.dailyenglish.presentation.screen.lessons.lesson.LessonScreen
import com.codework.dailyenglish.presentation.screen.lessons.lesson.LessonViewModel
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEffect
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEvent
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonState
import com.codework.dailyenglish.presentation.screen.lessons.review.*
import com.codework.dailyenglish.presentation.screen.lessons.review.mvi.QuizReviewEffect
import com.codework.dailyenglish.presentation.screen.lessons.review.mvi.QuizReviewEvent
import com.codework.dailyenglish.presentation.screen.lessons.review.mvi.QuizReviewState
import com.codework.dailyenglish.presentation.screen.lessons.sentence_builder.*
import com.codework.dailyenglish.presentation.screen.lessons.sentence_builder.mvi.SentenceBuilderEffect
import com.codework.dailyenglish.presentation.screen.lessons.sentence_builder.mvi.SentenceBuilderEvent
import com.codework.dailyenglish.presentation.screen.lessons.sentence_builder.mvi.SentenceBuilderState
import com.codework.dailyenglish.presentation.screen.lessons.speaking.*
import com.codework.dailyenglish.presentation.screen.lessons.speaking.mvi.SpeakingEffect
import com.codework.dailyenglish.presentation.screen.lessons.speaking.mvi.SpeakingEvent
import com.codework.dailyenglish.presentation.screen.lessons.speaking.mvi.SpeakingState
import com.codework.dailyenglish.presentation.screen.more.*
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreEffect
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreEvent
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreState
import com.codework.dailyenglish.presentation.screen.practice.*
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEffect
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEvent
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeState
import com.codework.dailyenglish.presentation.screen.translate.*
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateEffect
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateEvent
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateState
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailState
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailEvent
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailEffect
import com.codework.dailyenglish.presentation.screen.flashcard.FlashcardScreen
import com.codework.dailyenglish.presentation.screen.flashcard.FlashcardViewModel
import com.codework.dailyenglish.presentation.screen.flashcard.mvi.FlashcardEffect
import com.codework.dailyenglish.presentation.screen.flashcard.mvi.FlashcardEvent
import com.codework.dailyenglish.presentation.screen.flashcard.mvi.FlashcardState
import com.codework.dailyenglish.presentation.screen.leaderboard.LeaderboardScreen
import com.codework.dailyenglish.presentation.screen.leaderboard.LeaderboardViewModel
import com.codework.dailyenglish.presentation.screen.leaderboard.mvi.LeaderboardEffect
import com.codework.dailyenglish.presentation.screen.leaderboard.mvi.LeaderboardEvent
import com.codework.dailyenglish.presentation.screen.leaderboard.mvi.LeaderboardState
import com.codework.dailyenglish.presentation.screen.topic.TopicScreen
import com.codework.dailyenglish.presentation.screen.topic.TopicViewModel
import com.codework.dailyenglish.presentation.screen.topic.mvi.TopicEffect
import com.codework.dailyenglish.presentation.screen.topic.mvi.TopicEvent
import com.codework.dailyenglish.presentation.screen.topic.mvi.TopicState
import com.codework.dailyenglish.presentation.screen.vocab_list.VocabularyListScreen
import com.codework.dailyenglish.presentation.screen.vocab_list.VocabularyListViewModel
import com.codework.dailyenglish.presentation.screen.vocab_list.mvi.VocabularyListEffect
import com.codework.dailyenglish.presentation.screen.vocab_list.mvi.VocabularyListEvent
import com.codework.dailyenglish.presentation.screen.vocab_list.mvi.VocabularyListState
import com.codework.dailyenglish.presentation.screen.word_detail.WordDetailScreen
import com.codework.dailyenglish.presentation.screen.word_detail.WordDetailViewModel
import com.codework.dailyenglish.presentation.screen.word_detail.mvi.WordDetailEffect
import com.codework.dailyenglish.presentation.screen.word_detail.mvi.WordDetailEvent
import com.codework.dailyenglish.presentation.screen.word_detail.mvi.WordDetailState
import kotlinx.coroutines.delay


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Any // Thay đổi: nhận startDestination động từ ngoài truyền vào
) {
    NavHost(
        navController = navController,
        startDestination = startDestination, // Áp dụng ở đây
        modifier = Modifier.padding(bottom = 0.dp)
    ) {
        composableScreen<Route.LoginScreen, LoginViewModel, LoginState, LoginEvent, LoginEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is LoginEffect.NavigateBack -> {}
                    is LoginEffect.NavigateToSignUp -> {}
                    is LoginEffect.NavigateToEmailLogin -> {}
                    is LoginEffect.NavigateToHome -> {
                        // Khi Login thành công, xóa LoginScreen khỏi BackStack
                        navController.navigate(Route.HomeScreen) {
                            popUpTo(Route.LoginScreen) { inclusive = true }
                        }
                    }
                }
            }
        ) { state, onEvent ->
            LoginScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.HomeScreen, HomeViewModel, HomeState, HomeEvent, HomeEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is HomeEffect.NavigateToAi -> navController.navigate(Route.AIScreen)
                    is HomeEffect.NavigateToDictionary -> navController.navigate(Route.DictionaryScreen)
                    is HomeEffect.NavigateToSavedWord -> navController.navigate(Route.SavedWordsScreen)
                    is HomeEffect.NavigateToTranslate -> navController.navigate(Route.TranslateScreen)
                }
            }
        ) { state, onEvent ->
            HomeScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.CourseScreen, CourseViewModel, CourseState, CourseEvent, CourseEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is CourseEffect.NavigateToCourseDetails -> {
                        navController.navigate(
                            Route.CourseDetailScreen(
                                courseId = effect.courseId,
                                courseCode = effect.courseCode
                            )
                        )
                    }
                }
            }
        ) { state, onEvent ->
            val currentEntry by navController.currentBackStackEntryAsState()

            LaunchedEffect(currentEntry) {
                val entry = currentEntry ?: return@LaunchedEffect
                if (entry.destination.route?.contains("CourseScreen") == true) {
                    val savedStateHandle = entry.savedStateHandle
                    val shouldRefresh = savedStateHandle.get<Boolean>("should_refresh") ?: false

                    if (shouldRefresh) {
                        onEvent(CourseEvent.OnRefreshData)
                        savedStateHandle["should_refresh"] = false
                    }
                }
            }

            CourseScreen(state = state, onEvent = onEvent)
        }

        composableScreen<
                Route.LessonScreen, LessonViewModel, LessonState, LessonEvent, LessonEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is LessonEffect.NavigateBack -> navController.popBackStack()
                    is LessonEffect.NavigateToResult -> {
                        navController.navigate(
                            Route.LessonResultScreen(
                                scorePercentage = effect.scorePercentage,
                                starsEarned = effect.starsEarned,
                                lessonId = effect.lessonId
                            )
                        )
                    }
                }
            }
        ) { state, onEvent ->
            LessonScreen(state = state, onEvent = onEvent)
        }

        composable<Route.LessonResultScreen> { backStackEntry ->

            val route = backStackEntry.toRoute<Route.LessonResultScreen>()

            LaunchedEffect(Unit) {
                delay(2000)
                try {
                    navController.getBackStackEntry<Route.CourseDetailScreen>()
                        .savedStateHandle["should_refresh"] = true
                } catch (_: IllegalArgumentException) {
                }
                navController.popBackStack<Route.CourseDetailScreen>(inclusive = false)
            }
            AppCongratulationsScreen(
                scorePercentage = route.scorePercentage,
                starsEarned = route.starsEarned
            )
        }

        // Trong file NavGraph.kt của bạn
        composableScreen<
                Route.PracticeScreen,
                PracticeViewModel,
                PracticeState,
                PracticeEvent,
                PracticeEffect
                >(
            onNavigate = { effect ->
                when (effect) {
                    is PracticeEffect.NavigateToAiSpeaking -> {
                        navController.navigate(
                            Route.AiSpeakingScreen(
                                topicId = effect.topicId,
                                topicName = effect.topicName
                            )
                        )
                    }

                    is PracticeEffect.NavigateToTopicSelection -> {
                        navController.navigate(Route.TopicScreen(origin = effect.origin))
                    }

                    is PracticeEffect.NavigateToLeaderBoard -> {
                        navController.navigate(Route.LeaderboardScreen)
                    }

                    else -> {}
                }
            }
        ) { state, onEvent ->
            PracticeScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.MoreScreen, MoreViewModel, MoreState, MoreEvent, MoreEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is MoreEffect.NavigateToEditProfile -> { /* TODO: Chuyển sang màn Edit Profile */
                    }

                    is MoreEffect.HandleLogout -> {
                        navController.navigate(Route.LoginScreen) {
                            popUpTo(0) { inclusive = true }
                        }
                    }

                    is MoreEffect.NavigateToMenu -> { /* TODO: Xử lý click menu phụ dựa vào effect.label */
                    }

                    is MoreEffect.ShowToast -> { /* Hiện toast */
                    }
                }
            }
        ) { state, onEvent ->
            MoreScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.AIScreen, AiViewModel, AiState, AiEvent, AiEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is AiEffect.ShowToast -> { /* TODO: Xử lý hiện Toast hoặc Snackbar báo lỗi mạng */
                    }

                    is AiEffect.OpenGallery -> { /* TODO: Gọi intent mở thư viện ảnh */
                    }

                    is AiEffect.OpenMicrophone -> { /* TODO: Gọi intent mở Voice Input Google */
                    }
                }
            }
        ) { state, onEvent ->
            AiScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.DictionaryScreen, DictionaryViewModel, DictionaryState, DictionaryEvent, DictionaryEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is DictionaryEffect.PlayAudio -> {
                    }

                    is DictionaryEffect.NavigateToTopicWords -> {
                        navController.navigate(
                            Route.TopicWordsScreen(
                                topicId = effect.topicPublicId,
                                topicName = effect.topicName
                            )
                        )
                    }

                    is DictionaryEffect.NavigateToWordDetail -> {
                        navController.navigate(
                            Route.WordDetailScreen(
                                wordId = effect.wordId
                            )
                        )
                    }

                    is DictionaryEffect.NavigateToTopicSelection -> {
                        navController.navigate(
                            Route.TopicScreen(
                                origin = effect.origin
                            )
                        )
                    }

                }
            }
        ) { state, onEvent ->
            DictionaryScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.TranslateScreen, TranslateViewModel, TranslateState, TranslateEvent, TranslateEffect>(
            onNavigate = { effect ->
                when (effect) {
                    TranslateEffect.NavigateToHistory -> TODO()
                    TranslateEffect.NavigateToCameraScanner -> TODO()
                    else -> {}
                }
            }
        ) { state, onEvent ->
            TranslateScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.QuizReviewScreen, QuizReviewViewModel, QuizReviewState, QuizReviewEvent, QuizReviewEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is QuizReviewEffect.NavigateBack -> navController.popBackStack()
                    is QuizReviewEffect.NavigateToListening -> navController.navigate(Route.ListeningScreen)
                    is QuizReviewEffect.NavigateToGrammar -> navController.navigate(Route.SentenceBuilderScreen)
                }
            }
        ) { state, onEvent ->
            QuizReviewScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.GrammarScreen, GrammarViewModel, GrammarState, GrammarEvent, GrammarEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is GrammarEffect.NavigateBack -> navController.popBackStack()
                    is GrammarEffect.NavigateToTopic -> {
                        navController.navigate(Route.LessonScreen)
                    }

                    is GrammarEffect.OpenSearch -> { /* TODO: Mở thanh tìm kiếm */
                    }

                    is GrammarEffect.ShowToast -> {
                    }
                }
            }
        ) { state, onEvent ->
            GrammarScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.SentenceBuilderScreen, SentenceBuilderViewModel, SentenceBuilderState, SentenceBuilderEvent, SentenceBuilderEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is SentenceBuilderEffect.NavigateBack -> navController.popBackStack()
                    is SentenceBuilderEffect.NavigateToSpeaking -> navController.navigate(Route.SpeakingScreen)
                    is SentenceBuilderEffect.ShowToast -> { /* Hiện Toast */
                    }
                }
            }
        ) { state, onEvent ->
            SentenceBuilderScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.SpeakingScreen, SpeakingViewModel, SpeakingState, SpeakingEvent, SpeakingEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is SpeakingEffect.NavigateBack -> navController.popBackStack()
                    is SpeakingEffect.NavigateToFinish -> {
                        navController.popBackStack(Route.CourseScreen, inclusive = false)
                    }

                    is SpeakingEffect.PlayTtsAudio -> {
                    }

                    is SpeakingEffect.ShowToast -> {
                    }
                }
            }
        ) { state, onEvent ->
            SpeakingScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.CourseDetailScreen, CourseDetailViewModel, CourseDetailState, CourseDetailEvent, CourseDetailEffect>(
            onNavigate = { effect ->
                when (effect) {
                    else -> {}
                }
            }
        ) { state, onEvent ->
            val currentEntry by navController.currentBackStackEntryAsState()
            LaunchedEffect(currentEntry) {
                val entry = currentEntry ?: return@LaunchedEffect
                if (entry.destination.route?.contains("CourseDetailScreen") == true) {
                    val savedStateHandle = entry.savedStateHandle
                    val shouldRefresh = savedStateHandle.get<Boolean>("should_refresh") ?: false
                    if (shouldRefresh) {
                        onEvent(CourseDetailEvent.OnRefresh)
                        savedStateHandle["should_refresh"] = false
                    }
                }
            }

            CourseDetailsScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("should_refresh", true)
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        composableScreen<
                Route.SavedWordsScreen,
                VocabularyListViewModel,
                VocabularyListState,
                VocabularyListEvent,
                VocabularyListEffect
                >(
            onNavigate = { effect ->
                when (effect) {
                    is VocabularyListEffect.NavigateToFlashCard -> {
                        navController.navigate(Route.FlashcardScreen)
                    }
                }
            }
        ) { state, onEvent ->
            VocabularyListScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composableScreen<
                Route.TopicWordsScreen,
                VocabularyListViewModel,
                VocabularyListState,
                VocabularyListEvent,
                VocabularyListEffect
                >(
            onNavigate = { effect ->
                when (effect) {
                    is VocabularyListEffect.NavigateToFlashCard -> {
                        navController.navigate(Route.FlashcardScreen)
                    }
                }
            }
        ) { state, onEvent ->
            VocabularyListScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }


        composableScreen<Route.FlashcardScreen, FlashcardViewModel, FlashcardState, FlashcardEvent, FlashcardEffect>(
            onNavigate = { effect ->
                when (effect) {
                    else -> {}
                }
            }
        ) { state, onEvent ->
            FlashcardScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composableScreen<
                Route.LeaderboardScreen,
                LeaderboardViewModel,
                LeaderboardState,
                LeaderboardEvent,
                LeaderboardEffect
                >(
            onNavigate = { effect ->
            }
        ) { state, _ ->
            LeaderboardScreen(
                state = state,
                onBackClick = { navController.popBackStack() }
            )
        }

        composableScreen<
                Route.WordDetailScreen,
                WordDetailViewModel,
                WordDetailState,
                WordDetailEvent,
                WordDetailEffect
                >(
            onNavigate = { effect ->
                when (effect) {
                    else -> {}
                }
            }
        ) { state, onEvent ->
            WordDetailScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composableScreen<
                Route.TopicScreen,
                TopicViewModel,
                TopicState,
                TopicEvent,
                TopicEffect
                >(
            onNavigate = { effect ->
                when (effect) {
                    is TopicEffect.NavigateToVocabList -> {
                        navController.navigate(
                            Route.TopicWordsScreen(
                                topicId = effect.topicId,
                                topicName = effect.topicName
                            )
                        )
                    }

                    is TopicEffect.NavigateToAiPractice -> {
                        navController.navigate(
                            Route.AiSpeakingScreen(
                                topicId = effect.topicId,
                                topicName = effect.topicName
                            )
                        )
                    }
                }
            }
        ) { state, onEvent ->
            TopicScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composableScreen<
                Route.AiSpeakingScreen,
                AiSpeakingViewModel,
                AiSpeakingState,
                AiSpeakingEvent,
                AiSpeakingEffect
                >(
            onNavigate = { effect ->
                when (effect) {
                    is AiSpeakingEffect.StartSpeechRecognition -> {

                    }
                }
            }
        ) { state, onEvent ->
            AiSpeakingScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }


    }


}