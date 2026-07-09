package com.codework.dailyenglish.presentation.screen.subscription.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.subscription.mvi.SubscriptionEvent
import com.codework.dailyenglish.presentation.screen.subscription.mvi.SubscriptionState

@Composable
fun SubscriptionSheetContent(
    state: SubscriptionState,
    onEvent: (SubscriptionEvent) -> Unit,
    onClose: () -> Unit
) {
    val BluePrimary = Color(0xFF2196F3)
    val textDark = Color(0xFF1E293B)
    val textLight = Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = textDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        when {
            state.isPremium && !state.forceShowPacks -> {
                ActiveVIPDashboardContent(
                    expiryDate = state.premiumExpiresAt,
                    onBuyMoreClick = { onEvent(SubscriptionEvent.OnShowPacksClick) }, // Nhấn nút này sẽ kích hoạt OnShowPacksClick
                    onClose = onClose,
                    BluePrimary = BluePrimary
                )
            }
            state.paymentUiState.data != null || state.paymentUiState.isLoading -> {
                StateWrapper(
                    uiState = state.paymentUiState,
                    onRetry = { onEvent(SubscriptionEvent.OnBuyClick) }
                ) { paymentDetails ->
                    if (paymentDetails != null) {
                        PaymentSectionContent(
                            paymentDetails = paymentDetails,
                            isPaymentSuccess = state.isPaymentSuccess,
                            onBackToSelection = { onEvent(SubscriptionEvent.OnDismissBottomSheet) },
                            onClose = onClose,
                            BluePrimary = BluePrimary
                        )
                    }
                }
            }
            else -> {
                // Điểm đích: Màn giới thiệu và chọn gói (Trang 1)
                StateWrapper(
                    uiState = state.packsUiState,
                    onRetry = { onEvent(SubscriptionEvent.OnInitLoad) }
                ) {
                    IntroAndPacksSectionContent(
                        state = state,
                        onEvent = onEvent,
                        BluePrimary = BluePrimary,
                        textDark = textDark,
                        textLight = textLight
                    )
                }
            }
        }

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}