package com.codework.dailyenglish.domain.auth.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.auth.model.User
import com.codework.dailyenglish.domain.auth.repository.AuthRepository
import java.io.File
import javax.inject.Inject

class UpdateMyProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(fullName: String, avatarFile: File?): AppResult<User> {
        return authRepository.updateMyProfile(fullName, avatarFile)
    }
}