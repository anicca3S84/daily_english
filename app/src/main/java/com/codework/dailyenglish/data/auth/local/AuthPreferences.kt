package com.codework.dailyenglish.data.auth.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class AuthPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        private const val KEY_USER_ID = "PREF_KEY_USER_ID"
        private const val KEY_USER_EMAIL = "PREF_KEY_USER_EMAIL"
        private const val KEY_USER_NAME = "PREF_KEY_USER_NAME"
        private const val KEY_USER_AVATAR = "PREF_KEY_USER_AVATAR"
    }

    // Lưu cả cặp (Dùng khi Login mới)
    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
        }
    }

    // Lưu lẻ Access Token (Dùng trong Authenticator)
    fun saveAccessToken(token: String) {
        sharedPreferences.edit { putString(KEY_ACCESS_TOKEN, token) }
    }

    // Lưu lẻ Refresh Token (Dùng trong Authenticator)
    fun saveRefreshToken(token: String) {
        sharedPreferences.edit { putString(KEY_REFRESH_TOKEN, token) }
    }

    fun saveUser(userId: String, email: String, fullName: String?, avatarUrl: String?) {
        sharedPreferences.edit {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, fullName)
            putString(KEY_USER_AVATAR, avatarUrl)
        }
    }

    fun getAccessToken(): String? = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)

    fun clearAll() {
        sharedPreferences.edit { clear() }
    }

    // Thêm các hàm getter này vào class AuthPreferences
    fun getUserId(): String? = sharedPreferences.getString(KEY_USER_ID, null)

    fun getEmail(): String? = sharedPreferences.getString(KEY_USER_EMAIL, null)

    fun getFullName(): String? = sharedPreferences.getString(KEY_USER_NAME, null)

    fun getAvatarUrl(): String? = sharedPreferences.getString(KEY_USER_AVATAR, null)
}