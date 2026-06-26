package com.codework.dailyenglish.data.leaderboard.remote.dto

import com.google.gson.annotations.SerializedName

data class LeaderBoardWrapperDto(
    @SerializedName("leaderboardPage") val leaderboardPage: LeaderBoardPageDto,
    @SerializedName("myRank") val myRank: LeaderBoardDto
)