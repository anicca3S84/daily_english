package com.codework.daily_english.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState // NHỚ IMPORT UISTATE CỦA BẠN VÀO ĐÂY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <Effect> CollectEffect(
    effectFlow: Flow<Effect>,
    onEffect: (Effect) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(effectFlow, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                effectFlow.collect { effect ->
                    onEffect(effect)
                }
            }
        }
    }
}

// Đã bọc UiState<S> ở tham số state của content lambda
inline fun <reified R : Route, reified VM : BaseViewModel<S, E, F>, S, E, F> NavGraphBuilder.composableScreen(
    crossinline onNavigate: (F) -> Unit,
    crossinline content: @Composable (state: UiState<S>, onEvent: (E) -> Unit) -> Unit
) {
    composable<R> {
        val viewModel = hiltViewModel<VM>()

        // state ở đây tự động hiểu là UiState<S> vì ViewModel đã quy định thế
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        CollectEffect(viewModel.effectFlow) { effect ->
            onNavigate(effect)
        }

        content(state, viewModel::onEvent)
    }
}