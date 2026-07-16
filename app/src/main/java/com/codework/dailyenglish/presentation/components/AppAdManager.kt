package com.codework.dailyenglish.presentation.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AppAdManager {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var lastShowTime = 0L
//    private const val COOLDOWN_TIME_MS = 3 * 1000
    private const val COOLDOWN_TIME_MS = 3 * 60 * 1000 * 199
    private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    // 1. Khai báo biến lưu trạng thái Premium toàn cục tại đây
    var isPremiumUser: Boolean = false

    fun initialize(context: Context) {
        loadAd(context.applicationContext)
    }

    private fun loadAd(context: Context) {
        // 2. Nếu là tài khoản Premium, KHÔNG tải quảng cáo để tiết kiệm băng thông và pin
        if (isPremiumUser || interstitialAd != null || isLoading) return
        isLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    fun showAdIfAllowed(activity: Activity, onAdFinished: () -> Unit) {
        // 3. Nếu là tài khoản Premium, bỏ qua quảng cáo ngay lập tức
        if (isPremiumUser) {
            onAdFinished()
            return
        }

        val currentTime = System.currentTimeMillis()
        val isCooldownOver = (currentTime - lastShowTime) >= COOLDOWN_TIME_MS

        if (isCooldownOver && interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    lastShowTime = System.currentTimeMillis()
                    loadAd(activity.applicationContext)
                    onAdFinished()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    interstitialAd = null
                    loadAd(activity.applicationContext)
                    onAdFinished()
                }
            }
            interstitialAd?.show(activity)
        } else {
            if (interstitialAd == null) {
                loadAd(activity.applicationContext)
            }
            onAdFinished()
        }
    }
}


fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}