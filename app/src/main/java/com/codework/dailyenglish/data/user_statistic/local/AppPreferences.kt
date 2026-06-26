package com.codework.dailyenglish.data.user_statistic.local

interface AppPreferences {
    fun saveUnsyncedSeconds(seconds: Int)
    fun getUnsyncedSeconds(): Int
}