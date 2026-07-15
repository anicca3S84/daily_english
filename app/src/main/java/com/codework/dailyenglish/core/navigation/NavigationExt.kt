package com.codework.dailyenglish.core.navigation

import android.content.Context // Bổ sung import này
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController // Bổ sung import này
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder // Bổ sung import này
import androidx.navigation.compose.composable
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.presentation.components.AppAdManager // Import helper quản lý ad
import com.codework.dailyenglish.presentation.components.findActivity // Import helper tìm activity
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

inline fun <reified R : Route, reified VM : BaseViewModel<S, E, F>, S, E, F>
        NavGraphBuilder.composableScreen(
    crossinline onNavigate: (F) -> Unit,
    crossinline content: @Composable (state: S, onEvent: (E) -> Unit) -> Unit
) {
    composable<R> {
        val viewModel = hiltViewModel<VM>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val context = LocalContext.current

        CollectEffect(viewModel.globalEffectFlow) { globalEffect ->
            when (globalEffect) {
                is BaseEffect.ShowToast -> {
                    Toast.makeText(
                        context,
                        globalEffect.message.asString(context),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is BaseEffect.ShowSnackbar -> {
                }
            }
        }

        CollectEffect(viewModel.effectFlow) { effect ->
            onNavigate(effect)
        }

        content(state, viewModel::onEvent)
    }
}

