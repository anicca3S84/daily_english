package com.codework.daily_english.data.auth.local


import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

class AuthPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        // Sau này bạn có thể thêm KEY_USER_ID, KEY_IS_PREMIUM... ở đây
    }

    fun saveAccessToken(token: String) {
        // Dùng apply() để ghi bất đồng bộ (nhanh hơn commit())
        sharedPreferences.edit { putString(KEY_ACCESS_TOKEN, token) }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun clearSession() {
        sharedPreferences.edit { remove(KEY_ACCESS_TOKEN) }
    }
}