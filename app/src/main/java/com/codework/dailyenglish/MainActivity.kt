package com.codework.dailyenglish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.codework.dailyenglish.domain.user_statistic.usecase.StudyTimeManagerUseCase
import com.codework.dailyenglish.presentation.MainScreen
import com.codework.dailyenglish.presentation.MainViewModel
import com.codework.dailyenglish.presentation.theme.DailyEnglishTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var studyTimeManager: StudyTimeManagerUseCase

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by mainViewModel.state.collectAsState()

            DailyEnglishTheme {
                if (!state.isLoading) {
                    MainScreen(
                        startDestination = state.startDestination,
                        onSaveSessionTime = { seconds ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                studyTimeManager.accumulateLocalSeconds(seconds)
                            }
                        }
                    )
                }
            }
        }
    }
}