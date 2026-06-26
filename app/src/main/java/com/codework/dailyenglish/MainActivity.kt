package com.codework.dailyenglish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import com.codework.dailyenglish.core.utils.AppTimeTracker
import com.codework.dailyenglish.domain.user_statistic.usecase.StudyTimeManagerUseCase
import com.codework.dailyenglish.domain.user_statistic.usecase.SyncStudyTimeUseCase
import com.codework.dailyenglish.presentation.MainScreen
import com.codework.dailyenglish.presentation.theme.DailyEnglishTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var studyTimeManager: StudyTimeManagerUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AppTimeTracker(
                onSaveSessionTime = { seconds ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        studyTimeManager.accumulateLocalSeconds(seconds)
                    }
                }
            )

            LaunchedEffect(Unit) {
                lifecycleScope.launch(Dispatchers.IO) {
                    studyTimeManager.syncIfEnoughTime()
                }
            }

            DailyEnglishTheme {
                MainScreen()
            }
        }
    }
}