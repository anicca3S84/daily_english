package com.codework.dailyenglish.domain.auth.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.auth.model.User
import com.codework.dailyenglish.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): AppResult<User> {
        return repository.loginWithGoogle(idToken)
    }
}