package com.codework.dailyenglish.data.user_statistic.local


import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(
    context: Context
) : AppPreferences {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "daily_english_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_UNSYNCED_SECONDS = "unsynced_study_seconds"
    }

    override fun saveUnsyncedSeconds(seconds: Int) {
        prefs.edit().putInt(KEY_UNSYNCED_SECONDS, seconds).apply()
    }

    override fun getUnsyncedSeconds(): Int {
        return prefs.getInt(KEY_UNSYNCED_SECONDS, 0)
    }
}