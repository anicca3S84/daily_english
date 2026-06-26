package com.codework.dailyenglish.domain.leaderboard.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardWrapper

interface LeaderBoardRepository {
    suspend fun getLeaderBoard(page: Int, size: Int): AppResult<LeaderBoardWrapper>
}