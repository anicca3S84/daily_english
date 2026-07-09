package com.codework.dailyenglish.domain.auth.model

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val displayName: String,
    val avatarUrl: String,
    val isPremium: Boolean,
    val premiumExpiresAt: String?
)