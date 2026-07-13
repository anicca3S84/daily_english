package com.codework.dailyenglish.presentation.screen.subscription
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.navigation.CollectEffect
import com.codework.dailyenglish.presentation.components.AppBottomSheet
import com.codework.dailyenglish.presentation.screen.subscription.components.SubscriptionSheetContent
import com.codework.dailyenglish.presentation.screen.subscription.mvi.*

@Composable
fun SubscriptionPopup(
    visible: Boolean,
    onDismiss: () -> Unit,
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CollectEffect(viewModel.effectFlow) { effect ->
        when (effect) {
            SubscriptionEffect.NavigateBack -> {
                viewModel.onEvent(SubscriptionEvent.OnDismissBottomSheet)
                onDismiss()
            }
        }
    }

    CollectEffect(viewModel.globalEffectFlow) { globalEffect ->
        when (globalEffect) {
            is BaseEffect.ShowToast -> {
                Toast.makeText(
                    context,
                    globalEffect.message.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    AppBottomSheet(
        visible = visible,

        onDismiss = {
            viewModel.onEvent(SubscriptionEvent.OnDismissBottomSheet)
            onDismiss()
        },
        enableDrag = true
    ) { animateClose ->
        SubscriptionSheetContent(
            state = state,
            onEvent = viewModel::onEvent,
            onClose = animateClose,
            onCopySuccess = { viewModel.onEvent(SubscriptionEvent.OnCopySuccess) }
        )
    }
}

