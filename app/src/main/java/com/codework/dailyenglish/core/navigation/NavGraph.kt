package com.codework.dailyenglish.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.codework.dailyenglish.presentation.screen.lessons.review.model.QuizType
import com.codework.dailyenglish.presentation.screen.ai.*
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEffect
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEvent
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiState
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


@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Route.LoginScreen,
        modifier = Modifier.padding(bottom = 0.dp)
    ) {

        composableScreen<Route.LoginScreen, LoginViewModel, LoginState, LoginEvent, LoginEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is LoginEffect.NavigateBack -> {}
                    is LoginEffect.NavigateToSignUp -> {}
                    is LoginEffect.NavigateToEmailLogin -> {}
                    is LoginEffect.NavigateToHome -> {
                        navController.navigate(Route.HomeScreen)
                    }
                }
            }
        ) { state, onEvent ->
            LoginScreen(
                state = state,
                onEvent = onEvent
            )
        }

        composableScreen<Route.HomeScreen, HomeViewModel, HomeState, HomeEvent, HomeEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is HomeEffect.NavigateToAi -> navController.navigate(Route.AIScreen)
                    is HomeEffect.NavigateToDictionary -> navController.navigate(Route.DictionaryScreen)
                    is HomeEffect.NavigateToGrammar -> navController.navigate(Route.CourseDetailScreen)
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
            CourseScreen(state = state, onEvent = onEvent)
        }

        composableScreen<
                Route.LessonScreen, LessonViewModel, LessonState, LessonEvent, LessonEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is LessonEffect.NavigateBack -> navController.popBackStack()
                    is LessonEffect.NavigateToResult -> {
                        navController.navigate(
                            Route.LessonScreen(
                                lessonId = effect.lessonId
                            )
                        ) {
                            popUpTo<Route.LessonScreen> { inclusive = true }
                        }
                    }
                    is LessonEffect.ShowQuitDialog -> {
                    }
                }
            }
        ) { state, onEvent ->
            LessonScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.PracticeScreen, PracticeViewModel, PracticeState, PracticeEvent, PracticeEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is PracticeEffect.NavigateToAiSpeaking -> navController.navigate(Route.AIScreen)
                    is PracticeEffect.NavigateToFlashcard -> navController.navigate(Route.VocabularyScreen)
                    is PracticeEffect.NavigateToCategory -> { /* TODO */
                    }

                    is PracticeEffect.NavigateToGame -> { /* TODO */
                    }

                    is PracticeEffect.ShowToast -> { /* Hiện toast */
                    }
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

                    is MoreEffect.HandleLogout -> { /* TODO: Xóa token, chuyển về màn Login */
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

                    is DictionaryEffect.NavigateToFeature -> {
                        when (effect.feature) {
                            "Saved" -> { /* navController.navigate(Route.SavedWords) */
                            }

                            "Flashcards" -> navController.navigate(Route.VocabularyScreen)
                            // ... các mục khác
                        }
                    }

                    is DictionaryEffect.ShowToast -> { /* Hiện toast */
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
                }
            }
        ) { state, onEvent ->
            TranslateScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() })
        }

//        composableScreen<Route.VocabularyScreen, VocabularyViewModel, VocabularyState, VocabularyEvent, VocabularyEffect>(
//            onNavigate = { effect ->
//                when (effect) {
//                    is VocabularyEffect.NavigateBack -> navController.popBackStack()
//                    is VocabularyEffect.NavigateToReview -> {
//                        // Import QuizType nếu NavGraph chưa có
//                        navController.navigate(Route.QuizReviewScreen(QuizType.VOCABULARY))
//                    }
//
//                    is VocabularyEffect.PlayAudio -> {
//                        // TODO: Gọi TextToSpeech/ExoPlayer để phát âm thanh dựa trên effect.region (UK/US)
//                    }
//                }
//            }
//        ) { state, onEvent ->
//            VocabularyScreen(
//                state = state,
//                onEvent = onEvent,
//                onBackClick = { navController.popBackStack() })
//        }

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

//        composableScreen<Route.ListeningScreen, ListeningViewModel, ListeningState, ListeningEvent, ListeningEffect>(
//            onNavigate = { effect ->
//                when(effect) {
//                    // TODO: Xử lý ListeningEffect ở đây
//                    else -> {}
//                }
//            }
//        ) { state, onEvent ->
//            ListeningScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
//        }

        composableScreen<Route.GrammarScreen, GrammarViewModel, GrammarState, GrammarEvent, GrammarEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is GrammarEffect.NavigateBack -> navController.popBackStack()
                    is GrammarEffect.NavigateToTopic -> {
                        navController.navigate(Route.LessonScreen)
                    }

                    is GrammarEffect.OpenSearch -> { /* TODO: Mở thanh tìm kiếm */
                    }

                    is GrammarEffect.ShowToast -> { /* Hiện toast */
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

                    is SpeakingEffect.ShowToast -> { /* Hiện toast */
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
                    // Nếu bạn vẫn muốn xử lý một số Effect khác từ ViewModel CourseDetail
                    // Nhưng riêng ShowLessonPreview thì giờ ta xử lý trực tiếp tại UI của Screen
                    else -> { /* Xử lý các điều hướng khác nếu có */ }
                }
            }
        ) { state, onEvent ->
            CourseDetailsScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = { navController.popBackStack() },
                navController = navController
            )
        }
    }
}