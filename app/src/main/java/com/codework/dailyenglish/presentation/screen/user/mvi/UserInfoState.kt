package com.codework.dailyenglish.presentation.screen.user.mvi

import com.codework.dailyenglish.core.base.BaseUiState

data class UserProfileData(
    val email: String = "",
    val fullName: String = "",
    val avatarUrl: String = "",
    val isPremium: Boolean = false,
    val premiumExpiresAt: String? = null
)

data class UserInfoState(
    val userInfoState: BaseUiState<UserProfileData> = BaseUiState(),
    val editedFullName: String = "",
    val editedAvatarUrl: String = "",
    val isUpdating: Boolean = false
)