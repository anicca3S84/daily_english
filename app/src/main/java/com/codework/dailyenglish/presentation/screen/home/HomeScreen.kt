package com.codework.dailyenglish.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.components.QuickActionSection
import com.codework.dailyenglish.presentation.screen.home.components.WeeklyProgressCard
import com.codework.dailyenglish.presentation.screen.home.components.HomeHeader
import com.codework.dailyenglish.presentation.screen.home.model.QuickActionItem
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEvent
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeState

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding()
                )
        ) {
            HomeContent(
                state = state,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            HomeHeader(
                name = state.userName,
                avatarUrl = state.avatarUrl,
            )
        }

        item {
            val actionList = listOf(
                QuickActionItem(
                    icon = R.drawable.ic_launcher_foreground,
                    label = "Anicca AI",
                    color = Color(0xFFE91E63),
                    onClick = { onEvent(HomeEvent.OnQuickActionGemini) }
                ),
                QuickActionItem(
                    icon = R.drawable.ic_launcher_foreground,
                    label = "Dictionary",
                    color = Color(0xFF2196F3),
                    onClick = { onEvent(HomeEvent.OnQuickActionDictionary) }
                ),
                QuickActionItem(
                    icon = R.drawable.ic_launcher_foreground,
                    label = "Saved Word",
                    color = Color(0xFFFF9800),
                    onClick = { onEvent(HomeEvent.OnQuickActionSavedWord) }
                ),
                QuickActionItem(
                    icon = R.drawable.ic_launcher_foreground,
                    label = "Translate",
                    color = Color(0xFF4CAF50),
                    onClick = { onEvent(HomeEvent.OnQuickActionTranslate) }
                )
            )
            QuickActionSection(actions = actionList)
        }

        item {
            Text(text = "Learning Statistics", style = MaterialTheme.typography.titleMedium)

                // Dựa vào weeklyStatsState để quyết định hiển thị
            Spacer(modifier = Modifier.height(16.dp))


            when {
                    state.weeklyStatsState.isLoading -> {
                        CircularProgressIndicator() // Hoặc Shimmer Loading
                    }
                    state.weeklyStatsState.error != null -> {
                        Text(text = "Error: ${state.weeklyStatsState.error}")
                    }
                    else -> {
                        WeeklyProgressCard(weeklyStats = state.weeklyStatsState.data ?: emptyList())
                    }
                }
        }
    }
}