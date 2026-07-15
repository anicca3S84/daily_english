package com.codework.dailyenglish.data.auth.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.auth.remote.dto.AuthDto
import com.codework.dailyenglish.data.auth.remote.dto.UserDto
import java.io.File

interface AuthRemoteDataSource {
    suspend fun loginWithGoogle(idToken: String): AppResult<AuthDto>
    suspend fun getMyProfile(): AppResult<UserDto>
    suspend fun updateMyProfile(fullName: String, avatarFile: File?): AppResult<UserDto>
    suspend fun logout(refreshToken: String): AppResult<Unit>
}