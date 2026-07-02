package com.codework.dailyenglish.domain.auth.usecase

import com.codework.dailyenglish.domain.auth.repository.AuthRepository
import jakarta.inject.Inject

class CheckSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}