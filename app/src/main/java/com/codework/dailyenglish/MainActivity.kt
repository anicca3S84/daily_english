package com.codework.dailyenglish
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.codework.dailyenglish.presentation.MainScreen
import com.codework.dailyenglish.presentation.theme.DailyEnglishTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyEnglishTheme {
                MainScreen()
            }
        }
    }
}