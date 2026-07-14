package com.codework.dailyenglish.domain.leaderboard.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardWrapper
import com.codework.dailyenglish.domain.leaderboard.repository.LeaderBoardRepository
import javax.inject.Inject

class GetLeaderBoardUseCase @Inject constructor(
    private val repository: LeaderBoardRepository
) {
    suspend operator fun invoke(
        type: String,
        timeFilter: String,
        page: Int = 0,
        size: Int = 20
    ): AppResult<LeaderBoardWrapper> {
        return repository.getLeaderBoard(type, timeFilter, page, size)
    }
}