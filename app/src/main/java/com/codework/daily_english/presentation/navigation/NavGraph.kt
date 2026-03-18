package com.codework.daily_english.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

// Nhớ import NavigationExt.kt của bạn vào đây nếu nó nằm khác package
// import com.codework.daily_english.presentation.navigation.composableScreen

import com.codework.daily_english.presentation.screen.ai.*
import com.codework.daily_english.presentation.screen.courses.*
import com.codework.daily_english.presentation.screen.dictionary.*
import com.codework.daily_english.presentation.screen.grammar.*
import com.codework.daily_english.presentation.screen.home.*
import com.codework.daily_english.presentation.screen.lessons.*
import com.codework.daily_english.presentation.screen.lessons.review.*
import com.codework.daily_english.presentation.screen.lessons.sentence_builder.*
import com.codework.daily_english.presentation.screen.lessons.speaking.*
import com.codework.daily_english.presentation.screen.lessons.vocabulary.*
import com.codework.daily_english.presentation.screen.more.*
import com.codework.daily_english.presentation.screen.practice.*
import com.codework.daily_english.presentation.screen.translate.*

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Route.HomeScreen,
        modifier = Modifier.padding(bottom = 0.dp)
    ) {

        composableScreen<Route.HomeScreen, HomeViewModel, HomeState, HomeEvent, HomeEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is HomeEffect.NavigateToAi -> navController.navigate(Route.AIScreen)
                    is HomeEffect.NavigateToDictionary -> navController.navigate(Route.DictionaryScreen)
                    is HomeEffect.NavigateToGrammar -> navController.navigate(Route.GrammarScreen)
                    is HomeEffect.NavigateToTranslate -> navController.navigate(Route.SpeakingScreen) // Hoặc TranslateScreen tùy bạn setup
                }
            }
        ) { state, onEvent ->
            HomeScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.CourseScreen, CourseViewModel, CourseState, CourseEvent, CourseEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is CourseEffect.NavigateToLesson -> navController.navigate(Route.LessonScreen)
                }
            }
        ) { state, onEvent ->
            CourseScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.LessonScreen, LessonViewModel, LessonState, LessonEvent, LessonEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is LessonEffect.NavigateToVocabulary -> navController.navigate(Route.VocabularyScreen)
                    is LessonEffect.NavigateToListening -> navController.navigate(Route.ListeningScreen)
                    is LessonEffect.NavigateToSentenceBuilder -> navController.navigate(Route.SentenceBuilderScreen)
                    is LessonEffect.NavigateToSpeaking -> navController.navigate(Route.SpeakingScreen)
                    // is LessonEffect.NavigateToQuiz -> navController.navigate(Route.QuizReviewScreen(QuizType...))
                    is LessonEffect.ShowToast -> {
                        // TODO: Hiện Toast báo lỗi (bạn có thể setup Snackbar sau)
                    }

                    LessonEffect.NavigateToQuiz -> TODO()
                }
            }
        ) { state, onEvent ->
            LessonScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.PracticeScreen, PracticeViewModel, PracticeState, PracticeEvent, PracticeEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is PracticeEffect.NavigateToAiSpeaking -> navController.navigate(Route.AIScreen)
                    is PracticeEffect.NavigateToFlashcard -> navController.navigate(Route.VocabularyScreen)
                    is PracticeEffect.NavigateToCategory -> { /* TODO: Điều hướng category */ }
                    is PracticeEffect.NavigateToGame -> { /* TODO: Điều hướng vào minigame */ }
                    is PracticeEffect.ShowToast -> { /* Hiện toast */ }
                }
            }
        ) { state, onEvent ->
            PracticeScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.MoreScreen, MoreViewModel, MoreState, MoreEvent, MoreEffect>(
            onNavigate = { effect ->
                when(effect) {
                        is MoreEffect.NavigateToEditProfile -> { /* TODO: Chuyển sang màn Edit Profile */ }
                        is MoreEffect.HandleLogout -> { /* TODO: Xóa token, chuyển về màn Login */ }
                        is MoreEffect.NavigateToMenu -> { /* TODO: Xử lý click menu phụ dựa vào effect.label */ }
                        is MoreEffect.ShowToast -> { /* Hiện toast */ }
                }
            }
        ) { state, onEvent ->
            MoreScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.AIScreen, AiViewModel, AiState, AiEvent, AiEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is AiEffect.ShowToast -> { /* TODO: Xử lý hiện Toast hoặc Snackbar báo lỗi mạng */ }
                    is AiEffect.OpenGallery -> { /* TODO: Gọi intent mở thư viện ảnh */ }
                    is AiEffect.OpenMicrophone -> { /* TODO: Gọi intent mở Voice Input Google */ }
                }
            }
        ) { state, onEvent ->
            AiScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.DictionaryScreen, DictionaryViewModel, DictionaryState, DictionaryEvent, DictionaryEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is DictionaryEffect.PlayAudio -> {
                    }
                    is DictionaryEffect.NavigateToFeature -> {
                        when(effect.feature) {
                            "Saved" -> { /* navController.navigate(Route.SavedWords) */ }
                            "Flashcards" -> navController.navigate(Route.VocabularyScreen)
                            // ... các mục khác
                        }
                    }
                    is DictionaryEffect.ShowToast -> { /* Hiện toast */ }
                }
            }
        ) { state, onEvent ->
            DictionaryScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.TranslateScreen, TranslateViewModel, TranslateState, TranslateEvent, TranslateEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is TranslateEffect.ShowToast -> { /* TODO: Xử lý hiện Toast */ }
                }
            }
        ) { state, onEvent ->
            TranslateScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.VocabularyScreen, VocabularyViewModel, VocabularyState, VocabularyEvent, VocabularyEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is VocabularyEffect.NavigateBack -> navController.popBackStack()
                    is VocabularyEffect.NavigateToReview -> {
                        // Import QuizType nếu NavGraph chưa có
                        navController.navigate(Route.QuizReviewScreen(com.codework.daily_english.presentation.model.QuizType.VOCABULARY))
                    }
                    is VocabularyEffect.PlayAudio -> {
                        // TODO: Gọi TextToSpeech/ExoPlayer để phát âm thanh dựa trên effect.region (UK/US)
                    }
                }
            }
        ) { state, onEvent ->
            VocabularyScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.QuizReviewScreen, QuizReviewViewModel, QuizReviewState, QuizReviewEvent, QuizReviewEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is QuizReviewEffect.NavigateBack -> navController.popBackStack()
                    is QuizReviewEffect.NavigateToListening -> navController.navigate(Route.ListeningScreen)
                    is QuizReviewEffect.NavigateToGrammar -> navController.navigate(Route.SentenceBuilderScreen)
                }
            }
        ) { state, onEvent ->
            QuizReviewScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
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
                when(effect) {
                    is GrammarEffect.NavigateBack -> navController.popBackStack()
                    is GrammarEffect.NavigateToTopic -> {
                        navController.navigate(Route.LessonScreen)
                    }
                    is GrammarEffect.OpenSearch -> { /* TODO: Mở thanh tìm kiếm */ }
                    is GrammarEffect.ShowToast -> { /* Hiện toast */ }
                }
            }
        ) { state, onEvent ->
            GrammarScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.SentenceBuilderScreen, SentenceBuilderViewModel, SentenceBuilderState, SentenceBuilderEvent, SentenceBuilderEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is SentenceBuilderEffect.NavigateBack -> navController.popBackStack()
                    is SentenceBuilderEffect.NavigateToSpeaking -> navController.navigate(Route.SpeakingScreen)
                    is SentenceBuilderEffect.ShowToast -> { /* Hiện Toast */ }
                }
            }
        ) { state, onEvent ->
            SentenceBuilderScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
        }

        composableScreen<Route.SpeakingScreen, SpeakingViewModel, SpeakingState, SpeakingEvent, SpeakingEffect>(
            onNavigate = { effect ->
                when(effect) {
                    is SpeakingEffect.NavigateBack -> navController.popBackStack()
                    is SpeakingEffect.NavigateToFinish -> {
                        navController.popBackStack(Route.CourseScreen, inclusive = false)
                    }
                    is SpeakingEffect.PlayTtsAudio -> {
                    }
                    is SpeakingEffect.ShowToast -> { /* Hiện toast */ }
                }
            }
        ) { state, onEvent ->
            SpeakingScreen(state = state, onEvent = onEvent, onBackClick = { navController.popBackStack() })
        }
    }
}