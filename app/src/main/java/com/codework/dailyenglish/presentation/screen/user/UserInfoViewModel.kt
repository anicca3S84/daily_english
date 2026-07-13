package com.codework.dailyenglish.presentation.screen.user

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.auth.usecase.GetMyProfileUseCase
import com.codework.dailyenglish.domain.auth.usecase.UpdateMyProfileUseCase
import com.codework.dailyenglish.presentation.screen.user.mvi.UserInfoEffect
import com.codework.dailyenglish.presentation.screen.user.mvi.UserInfoEvent
import com.codework.dailyenglish.presentation.screen.user.mvi.UserInfoState
import com.codework.dailyenglish.presentation.screen.user.mvi.UserProfileData
import com.codework.dailyenglish.presentation.utils.UiText
import com.codework.dailyenglish.presentation.utils.asUiText
import com.codework.dailyenglish.presentation.utils.compressImage
import com.codework.dailyenglish.presentation.utils.toFile // Import hàm ext vừa viết
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val updateMyProfileUseCase: UpdateMyProfileUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel<UserInfoState, UserInfoEvent, UserInfoEffect>(UserInfoState()) {

    private var selectedAvatarUri: Uri? = null

    init {
        fetchUserProfile()
    }

    override fun onEvent(event: UserInfoEvent) {
        when (event) {
            is UserInfoEvent.OnLoadProfile -> fetchUserProfile()
            is UserInfoEvent.OnFullNameChanged -> {
                _state.update { it.copy(editedFullName = event.newName) }
            }
            is UserInfoEvent.OnAvatarChanged -> {
                _state.update { it.copy(editedAvatarUrl = event.newUrl) }
            }
            is UserInfoEvent.OnAvatarSelected -> handleAvatarSelected(event.uri)
            is UserInfoEvent.OnSaveProfile -> handleSaveProfile()
        }
    }

    private fun handleAvatarSelected(uri: Uri) {
        selectedAvatarUri = uri
        _state.update { it.copy(editedAvatarUrl = uri.toString()) }
    }

    private fun fetchUserProfile() {
        _state.update { it.copy(userInfoState = it.userInfoState.copy(isLoading = true, error = null)) }
        viewModelScope.launch {
            getMyProfileUseCase()
                .onSuccess { user ->
                    val profileData = UserProfileData(
                        email = user.email,
                        fullName = user.fullName ?: "",
                        avatarUrl = user.avatarUrl ?: "",
                        isPremium = user.isPremium,
                        premiumExpiresAt = user.premiumExpiresAt
                    )
                    selectedAvatarUri = null
                    _state.update { currentState ->
                        currentState.copy(
                            userInfoState = currentState.userInfoState.copy(
                                isLoading = false,
                                data = profileData
                            ),
                            editedFullName = profileData.fullName,
                            editedAvatarUrl = profileData.avatarUrl
                        )
                    }
                }
                .onError { err ->
                    _state.update {
                        it.copy(
                            userInfoState = it.userInfoState.copy(
                                isLoading = false,
                                error = err.asUiText()
                            )
                        )
                    }
                }
        }
    }

    private fun handleSaveProfile() {
        viewModelScope.launch {
            val originalData = _state.value.userInfoState.data
            val updatedName = _state.value.editedFullName.trim()

            if (updatedName.isEmpty()) {
                sendGlobalEffect(BaseEffect.ShowToast(UiText.DynamicString("Name cannot be empty")))
                return@launch
            }

            if (originalData != null &&
                updatedName == originalData.fullName &&
                selectedAvatarUri == null
            ) {
                sendGlobalEffect(BaseEffect.ShowToast(UiText.DynamicString("No changes made")))
                return@launch
            }

            _state.update { it.copy(isUpdating = true) }

            val avatarFile: File? = selectedAvatarUri?.let { uri ->
                compressImage(context, uri, maxFileSizeKB = 500)
            }

            updateMyProfileUseCase(fullName = updatedName, avatarFile = avatarFile)
                .onSuccess { user ->
                    val profileData = UserProfileData(
                        fullName = user.fullName,
                        avatarUrl = user.avatarUrl,
                        email = user.email,
                        premiumExpiresAt = user.premiumExpiresAt,
                        isPremium = user.isPremium
                    )
                    selectedAvatarUri = null
                    _state.update { currentState ->
                        currentState.copy(
                            isUpdating = false,
                            userInfoState = currentState.userInfoState.copy(data = profileData),
                            editedFullName = profileData.fullName,
                            editedAvatarUrl = profileData.avatarUrl
                        )
                    }
                    sendGlobalEffect(BaseEffect.ShowToast(UiText.DynamicString("Profile updated successfully")))
                }
                .onError { err ->
                    _state.update { it.copy(isUpdating = false) }
                    sendGlobalEffect(BaseEffect.ShowToast(err.asUiText()))
                }
        }
    }
}