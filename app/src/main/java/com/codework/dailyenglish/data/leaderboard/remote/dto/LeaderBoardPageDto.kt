package com.codework.dailyenglish.data.leaderboard.remote.dto

import com.google.gson.annotations.SerializedName

data class LeaderBoardPageDto(
    @SerializedName("content") val content: List<LeaderBoardDto>
)