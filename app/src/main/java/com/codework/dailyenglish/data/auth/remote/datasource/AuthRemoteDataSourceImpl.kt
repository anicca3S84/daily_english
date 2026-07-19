package com.codework.dailyenglish.data.auth.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.data.auth.remote.AuthApi
import com.codework.dailyenglish.data.auth.remote.dto.AuthDto
import com.codework.dailyenglish.data.auth.remote.dto.GoogleLoginDto
import com.codework.dailyenglish.data.auth.remote.dto.LogoutRequestDto
import com.codework.dailyenglish.data.auth.remote.dto.UserDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApi
) : AuthRemoteDataSource {

    override suspend fun loginWithGoogle(idToken: String): AppResult<AuthDto> {
         return safeApiCall { api.loginWithGoogle(GoogleLoginDto(idToken)) }.unwrap()
    }

    override suspend fun getMyProfile(): AppResult<UserDto> {
        return safeApiCall { api.getMyProfile() }.unwrap()
    }

    override suspend fun updateMyProfile(fullName: String, avatarFile: File?): AppResult<UserDto> {
        return safeApiCall {
            // Cách này loại bỏ hoàn toàn charset=UTF-8 tự động của OkHttp
            val fullNamePart = MultipartBody.Part.createFormData("fullName", fullName)

            val avatarPart = avatarFile?.let { file ->
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("avatar", file.name, requestFile)
            }

            api.updateMyProfile(fullName = fullNamePart, avatar = avatarPart)
        }.unwrap()
    }

    override suspend fun logout(refreshToken: String): AppResult<Unit> {
        return safeApiCall { api.logout(LogoutRequestDto(refreshToken)) }.unwrap()
    }
}
