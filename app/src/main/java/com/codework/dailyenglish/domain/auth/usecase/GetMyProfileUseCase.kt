package com.codework.dailyenglish.domain.auth.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.auth.model.User
import com.codework.dailyenglish.domain.auth.repository.AuthRepository
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AppResult<User> {
        return authRepository.getMyProfile()
    }
}