package com.codework.dailyenglish

import android.app.Application
import com.codework.dailyenglish.presentation.components.AppAdManager
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DailyEnglishApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread {
            MobileAds.initialize(this) {
                AppAdManager.initialize(this)
            }
        }.start()
    }
}