package com.codework.daily_english.data.auth.mapper

import com.codework.daily_english.data.auth.remote.dto.UserDto
import com.codework.daily_english.domain.auth.model.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id ?: "",
        email = this.email ?: "",
        displayName = this.displayName ?: "Learner",
        avatarUrl = this.avatarUrl ?: "",
        isPremium = this.isPremium == 1
    )
}