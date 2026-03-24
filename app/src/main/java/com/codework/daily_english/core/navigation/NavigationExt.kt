package com.codework.daily_english.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.codework.daily_english.core.base.BaseEffect
import com.codework.daily_english.core.base.BaseViewModel
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

inline fun <reified R : Route, reified VM : BaseViewModel<S, E, F>, S, E, F> NavGraphBuilder.composableScreen(
    crossinline onNavigate: (BaseEffect) -> Unit,
    // Sửa State<S> thành S ở đây 👇
    crossinline content: @Composable (state: S, onEvent: (E) -> Unit) -> Unit
) {
    composable<R> {
        val viewModel = hiltViewModel<VM>()

        val state by viewModel.state.collectAsStateWithLifecycle()

        CollectEffect(viewModel.effectFlow) { effect ->
            onNavigate(effect)
        }

        // Truyền state (kiểu S) vào content
        content(state, viewModel::onEvent)
    }
}