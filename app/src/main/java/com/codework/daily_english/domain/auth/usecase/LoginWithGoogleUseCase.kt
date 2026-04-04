package com.codework.daily_english.domain.auth.usecase

import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.domain.auth.model.User
import com.codework.daily_english.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): AppResult<User> {
        return repository.loginWithGoogle(idToken)
    }
}