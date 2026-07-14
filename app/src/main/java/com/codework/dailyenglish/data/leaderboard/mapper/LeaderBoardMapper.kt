package com.codework.dailyenglish.data.leaderboard.mapper

import com.codework.dailyenglish.data.leaderboard.remote.dto.LeaderBoardDto
import com.codework.dailyenglish.data.leaderboard.remote.dto.LeaderBoardWrapperDto
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardProfile
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardWrapper

fun LeaderBoardDto.toDomain(): LeaderBoardProfile {
    return LeaderBoardProfile(
        userId = this.userId,
        fullName = this.fullName.orEmpty(),
        avatarUrl = this.avatarUrl.orEmpty(),
        score = this.score,
        rank = this.rank,
        isCurrentUser = this.isCurrentUser
    )
}

fun LeaderBoardWrapperDto.toDomain(): LeaderBoardWrapper {
    return LeaderBoardWrapper(
        leaderboardList = this.leaderboardPage.content.map { it.toDomain() },
        myRank = this.myRank.toDomain()
    )
}