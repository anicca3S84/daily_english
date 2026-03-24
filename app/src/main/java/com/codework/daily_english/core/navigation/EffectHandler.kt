package com.codework.daily_english.core.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.codework.daily_english.core.base.BaseEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <F> HandleEffect(
    effectFlow: Flow<F>,
    onNavigate: (F) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is BaseEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is BaseEffect.ShowSnackbar -> {
                    // Nếu bạn có SnackbarHostState toàn cục thì xử lý ở đây
                    // Hoặc tạm thời dùng Toast nếu chưa setup Snackbar
                }

                else -> {
                    // Nếu không phải effect chung (ví dụ: NavigateToDetail)
                    // thì gửi ra ngoài cho NavGraph xử lý
                    onNavigate(effect)
                }
            }
        }
    }
}

