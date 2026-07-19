package com.codework.dailyenglish.core.navigation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.codework.dailyenglish.presentation.screen.lessons.lesson_result.AppCongratulationsScreen
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
import com.codework.dailyenglish.core.session.SessionEvent
import com.codework.dailyenglish.presentation.MainViewModel
import com.codework.dailyenglish.presentation.components.AppAdManager // Import AdManager
import com.codework.dailyenglish.presentation.screen.user.UserInfoScreen
import com.codework.dailyenglish.presentation.screen.user.UserInfoViewModel
import com.codework.dailyenglish.presentation.screen.user.mvi.UserInfoEffect
import com.codework.dailyenglish.presentation.screen.user.mvi.UserInfoEvent
import com.codework.dailyenglish.presentation.screen.user.mvi.UserInfoState


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Any,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current // Lấy context của ứng dụng tại đây
    var showSessionExpiredDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mainViewModel.sessionEvents.collect { event ->
            if (event is SessionEvent.ForceLogout) {
                showSessionExpiredDialog = true
            }
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        navController.navigate(Route.HomeScreen) {
            popUpTo(Route.LoginScreen) { inclusive = true }
        }
    }

    val navigateToHomeWithPermissionCheck = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                // Đã có quyền, chuyển thẳng sang Home
                navController.navigate(Route.HomeScreen) {
                    popUpTo(Route.LoginScreen) { inclusive = true }
                }
            } else {
                // Chưa có quyền, kích hoạt hộp thoại xin quyền của hệ thống
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // Các phiên bản Android thấp hơn API 33 không cần xin quyền động
            navController.navigate(Route.HomeScreen) {
                popUpTo(Route.LoginScreen) { inclusive = true }
            }
        }
    }

    if (showSessionExpiredDialog) {
        AlertDialog(
            onDismissRequest = { /* Không cho tắt khi bấm ra ngoài */ },
            title = { Text(text = "Phiên đăng nhập hết hạn") },
            text = { Text(text = "Phiên làm việc của bạn đã kết thúc. Vui lòng đăng nhập lại để tiếp tục học tập.") },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate(Route.LoginScreen) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                ) {
                    Text(text = "Đăng nhập lại")
                }
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(bottom = 0.dp)
    ) {
        composableScreen<Route.LoginScreen, LoginViewModel, LoginState, LoginEvent, LoginEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is LoginEffect.NavigateBack -> {}
                    is LoginEffect.NavigateToSignUp -> {}
                    is LoginEffect.NavigateToEmailLogin -> {}
                    is LoginEffect.NavigateToHome -> {
                        navigateToHomeWithPermissionCheck()
                    }
                }
            }
        ) { state, onEvent ->
            LoginScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.HomeScreen, HomeViewModel, HomeState, HomeEvent, HomeEffect>(
            onNavigate = { effect ->
                when (effect) {
                    // Áp dụng quảng cáo giãn cách 3 phút cho các tính năng chính từ màn hình Home
                    is HomeEffect.NavigateToAi -> navController.navigateWithAd(context, Route.AIScreen)
                    is HomeEffect.NavigateToDictionary -> navController.navigateWithAd(context, Route.DictionaryScreen)
                    is HomeEffect.NavigateToSavedWord -> navController.navigateWithAd(context, Route.VocabularyListScreen)
                    is HomeEffect.NavigateToTranslate -> navController.navigateWithAd(context, Route.TranslateScreen)
                    is HomeEffect.NavigateToSubscription -> navController.navigate(Route.SubscriptionScreen) // Không quảng cáo khi người dùng muốn mua VIP
                    else -> {}
                }
            }
        ) { state, onEvent ->
            HomeScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.CourseScreen, CourseViewModel, CourseState, CourseEvent, CourseEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is CourseEffect.NavigateToCourseDetails -> {
                        navController.navigateWithAd(
                            context,
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

            LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
                onEvent(CourseEvent.OnCheckPremiumStatus)
            }

            CourseScreen(state = state, onEvent = onEvent)
        }

        composableScreen<
                Route.LessonScreen, LessonViewModel, LessonState, LessonEvent, LessonEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is LessonEffect.NavigateBack -> navController.popBackStack()
                    is LessonEffect.NavigateToResult -> {
                        // Chuyển sang màn hình kết quả sau khi hoàn thành bài học
                        navController.navigate(
                            Route.LessonResultScreen(
                                scorePercentage = effect.scorePercentage,
                                starsEarned = effect.starsEarned,
                                lessonId = effect.lessonId,
                                streak = effect.streak
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
                starsEarned = route.starsEarned,
                streak = route.streak,
            )
        }

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
                        navController.navigateWithAd(
                            context,
                            Route.AiSpeakingScreen(
                                topicId = effect.topicId,
                                topicName = effect.topicName
                            )
                        )
                    }

                    is PracticeEffect.NavigateToTopicSelection -> {
                        navController.navigateWithAd(context, Route.TopicScreen(origin = effect.origin))
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
                    is MoreEffect.NavigateToEditProfile -> {
                        navController.navigate(Route.UserInfoScreen)
                    }
                    is MoreEffect.HandleLogout -> {
                        navController.navigate(Route.LoginScreen) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    is MoreEffect.NavigateToMenu -> {
                        if (effect.label == "Personal") {
                            navController.navigate(Route.UserInfoScreen)
                        }
                    }
                    is MoreEffect.NavigateToLeaderBoard -> {
                        navController.navigateWithAd(context, Route.LeaderboardScreen)
                    }
                }
            }
        ) { state, onEvent ->
            val currentEntry by navController.currentBackStackEntryAsState()

            LaunchedEffect(currentEntry) {
                val entry = currentEntry ?: return@LaunchedEffect
                if (entry.destination.route?.contains("MoreScreen") == true) {
                    val savedStateHandle = entry.savedStateHandle
                    val shouldRefresh = savedStateHandle.get<Boolean>("should_refresh_profile") ?: false
                    if (shouldRefresh) {
                        onEvent(MoreEvent.OnRefreshData)
                        savedStateHandle["should_refresh_profile"] = false
                    }
                }
            }

            MoreScreen(state = state, onEvent = onEvent)
        }

        composableScreen<Route.UserInfoScreen, UserInfoViewModel, UserInfoState, UserInfoEvent, UserInfoEffect>(
            onNavigate = { effect ->
                when (effect) {
                }
            }
        ) { state, onEvent ->
            UserInfoScreen(
                state = state,
                onEvent = onEvent,
                onBackClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("should_refresh_profile", true)
                    navController.popBackStack()
                }
            )
        }

        composableScreen<Route.AIScreen, AiViewModel, AiState, AiEvent, AiEffect>(
            onNavigate = { effect ->
                when (effect) {
                    is AiEffect.OpenGallery -> { /* TODO: Gọi intent mở thư viện ảnh */
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
                        navController.navigateWithAd(
                            context,
                            Route.TopicWordsScreen(
                                topicId = effect.topicId,
                                topicName = effect.topicName
                            )
                        )
                    }

                    is DictionaryEffect.NavigateToWordDetail -> {
                        navController.navigateWithAd(
                            context,
                            Route.WordDetailScreen(
                                wordId = effect.wordId
                            )
                        )
                    }

                    is DictionaryEffect.NavigateToTopicSelection -> {
                        navController.navigateWithAd(
                            context,
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
                    is QuizReviewEffect.NavigateToListening -> navController.navigateWithAd(context, Route.ListeningScreen)
                    is QuizReviewEffect.NavigateToGrammar -> navController.navigateWithAd(context, Route.SentenceBuilderScreen)
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
                        navController.navigateWithAd(context, Route.LessonScreen)
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
                Route.VocabularyListScreen,
                VocabularyListViewModel,
                VocabularyListState,
                VocabularyListEvent,
                VocabularyListEffect
                >(
            onNavigate = { effect ->
                when (effect) {
                    is VocabularyListEffect.NavigateToFlashCard -> {
                        navController.navigateWithAd(context, Route.FlashcardScreen)
                    }
                }
            }
        ) { state, onEvent ->
            val currentBackStackEntry = navController.currentBackStackEntry
            LaunchedEffect(currentBackStackEntry) {
                currentBackStackEntry?.lifecycle?.addObserver(androidx.lifecycle.LifecycleEventObserver { _, event ->
                    if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                        onEvent(VocabularyListEvent.OnRefresh)
                    }
                })
            }

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
                        navController.navigateWithAd(context, Route.FlashcardScreen)
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
        ) { state, event ->
            LeaderboardScreen(
                state = state,
                onEvent = event,
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
                        navController.navigateWithAd(
                            context,
                            Route.TopicWordsScreen(
                                topicId = effect.topicId,
                                topicName = effect.topicName
                            )
                        )
                    }

                    is TopicEffect.NavigateToAiPractice -> {
                        navController.navigateWithAd(
                            context,
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

// =========================================================================
// HÀM MỞ RỘNG TIỆN ÍCH HỖ TRỢ CHUYỂN TRANG CÓ QUẢNG CÁO XEN KẼ
// BẠN CÓ THỂ ĐỂ LUÔN ĐOẠN NÀY DƯỚI ĐÂY MÀ KHÔNG CẦN TẠO FILE MỚI
// =========================================================================

fun NavController.navigateWithAd(
    context: Context,
    route: Any,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val activity = context.findActivity()
    if (activity != null) {
        AppAdManager.showAdIfAllowed(activity) {
            this.navigate(route, builder)
        }
    } else {
        this.navigate(route, builder)
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}