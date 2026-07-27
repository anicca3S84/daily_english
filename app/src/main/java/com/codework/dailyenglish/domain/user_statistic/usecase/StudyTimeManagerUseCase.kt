package com.codework.dailyenglish.domain.user_statistic.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.user_statistic.local.AppPreferences
import com.codework.dailyenglish.domain.user_statistic.repository.UserStatisticRepository
import jakarta.inject.Inject

class StudyTimeManagerUseCase @Inject constructor(
    private val localPreferences: AppPreferences,
    private val statisticRepository: UserStatisticRepository
) {
    fun accumulateLocalSeconds(newSeconds: Int) {
        val currentSeconds = localPreferences.getUnsyncedSeconds()
        localPreferences.saveUnsyncedSeconds(currentSeconds + newSeconds)
    }

    suspend fun syncIfEnoughTime() {
        val totalSeconds = localPreferences.getUnsyncedSeconds()

        if (totalSeconds >= 180) {
            val result = statisticRepository.syncStudyTime(totalSeconds)

            if (result is AppResult.Success) {
                localPreferences.saveUnsyncedSeconds(0)
            }
        }
    }
}