package com.codework.dailyenglish.presentation.screen.subscription

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.auth.usecase.GetMyProfileUseCase
import com.codework.dailyenglish.domain.lesson_preview.usecase.CheckPaymentStatusUseCase
import com.codework.dailyenglish.domain.subscription.usecase.BuyPremiumUseCase
import com.codework.dailyenglish.domain.subscription.usecase.GetSubscriptionPacksUseCase
import com.codework.dailyenglish.domain.user.usecase.GetUserInfoUseCase
import com.codework.dailyenglish.presentation.screen.subscription.mvi.*
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val getSubscriptionPacksUseCase: GetSubscriptionPacksUseCase,
    private val buyPremiumUseCase: BuyPremiumUseCase,
    private val checkPaymentStatusUseCase: CheckPaymentStatusUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : BaseViewModel<SubscriptionState, SubscriptionEvent, SubscriptionEffect>(
    initialState = SubscriptionState()
) {

    private var pollingJob: kotlinx.coroutines.Job? = null

    init {
        onEvent(SubscriptionEvent.OnInitLoad)
    }

    override fun onEvent(event: SubscriptionEvent) {
        when (event) {
            is SubscriptionEvent.OnInitLoad -> fetchSubscriptionPacks()
            is SubscriptionEvent.OnSelectPack -> _state.update { it.copy(selectedPackId = event.packId) }
            is SubscriptionEvent.OnBuyClick -> initiatePayment()
            is SubscriptionEvent.OnPaymentSuccessMock -> {
                _state.update { it.copy(isPaymentSuccess = true) }
            }
            is SubscriptionEvent.OnBackClick -> sendEffect(SubscriptionEffect.NavigateBack)
            is SubscriptionEvent.OnDismissBottomSheet -> {
                stopPollingPayment()
                _state.update {
                    it.copy(
                        paymentUiState = BaseUiState(),
                        isPaymentSuccess = false,
                        forceShowPacks = false // ĐÃ SỬA: Đưa cờ forceShowPacks về false khi đóng Popup
                    )
                }
            }

            // BỔ SUNG: Xử lý sự kiện ép hiển thị gói cước cho VIP mua thêm
            SubscriptionEvent.OnShowPacksClick -> {
                _state.update { it.copy(forceShowPacks = true) }
            }

            is SubscriptionEvent.OnPaymentSuccessMock -> {
                _state.update { it.copy(isPaymentSuccess = true) }
            }
        }
    }

    private fun fetchSubscriptionPacks() {
        // =========================================================================
        // --- BỔ SUNG: Đọc trạng thái VIP từ bộ nhớ Local ngay khi khởi tạo ---
        // =========================================================================
        viewModelScope.launch {
            val localUser = getUserInfoUseCase()
            if (localUser != null) {
                _state.update { it.copy(isPremium = localUser.isPremium) }
            }
        }

        // Luồng lấy gói cước từ API giữ nguyên
        _state.update { it.copy(packsUiState = it.packsUiState.copy(isLoading = true, error = null)) }
        viewModelScope.launch {
            getSubscriptionPacksUseCase()
                .onSuccess { packs ->
                    _state.update {
                        it.copy(
                            packsUiState = BaseUiState(data = packs, isLoading = false),
                            selectedPackId = packs.firstOrNull()?.id.orEmpty()
                        )
                    }
                }
                .onError { err ->
                    _state.update {
                        it.copy(packsUiState = BaseUiState(isLoading = false, error = err.asUiText()))
                    }
                    sendGlobalEffect(BaseEffect.ShowToast(err.asUiText()))
                }
        }
    }

    private fun initiatePayment() {
        val currentPackId = state.value.selectedPackId
        if (currentPackId.isEmpty()) return

        _state.update { it.copy(paymentUiState = it.paymentUiState.copy(isLoading = true, error = null)) }
        viewModelScope.launch {
            buyPremiumUseCase(currentPackId, "VIETQR")
                .onSuccess { paymentData ->
                    _state.update {
                        it.copy(paymentUiState = BaseUiState(data = paymentData, isLoading = false))
                    }
                    startPollingPayment(paymentData.transactionRef)
                }
                .onError { err ->
                    _state.update {
                        it.copy(paymentUiState = BaseUiState(isLoading = false, error = err.asUiText()))
                    }
                    sendGlobalEffect(BaseEffect.ShowToast(err.asUiText()))
                }
        }
    }

    private fun startPollingPayment(transactionRef: String) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(2000)
                checkPaymentStatusUseCase(transactionRef)
                    .onSuccess { status ->
                        if (status == "SUCCESS") {
                            stopPollingPayment()
                            syncUserProfileAndFinish()
                        }
                    }
                    .onError {
                        // Bỏ qua lỗi mạng cục bộ trong lúc polling
                    }
            }
        }
    }

    private fun syncUserProfileAndFinish() {
        viewModelScope.launch {
            getMyProfileUseCase()
                .onSuccess { userProfile ->
                    _state.update {
                        it.copy(
                            isPaymentSuccess = true,
                            isPremium = userProfile.isPremium,
                            premiumExpiresAt = userProfile.premiumExpiresAt ?: "December 12, 2026"
                        )
                    }
                }
                .onError {
                    _state.update {
                        it.copy(
                            isPaymentSuccess = true,
                            isPremium = true
                        )
                    }
                }
        }
    }

    private fun stopPollingPayment() {
        pollingJob?.cancel()
        pollingJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopPollingPayment()
    }
}