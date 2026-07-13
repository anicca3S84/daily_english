package com.codework.dailyenglish.domain.auth.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.auth.model.User
import java.io.File

interface AuthRepository {
    suspend fun loginWithEmail(email: String, password: String): AppResult<User>
    suspend fun loginWithGoogle(idToken: String): AppResult<User>
    suspend fun loginWithApple(authCode: String): AppResult<User>
    suspend fun getUserInfo(): User?
    suspend fun getAccessToken(): String?
    suspend fun isUserLoggedIn(): Boolean
    suspend fun logout()
    suspend fun getMyProfile(): AppResult<User>
    suspend fun updateMyProfile(fullName: String, avatarFile: File?): AppResult<User>
}