package com.nezuko.auth.dto

data class ValidateRequest(
    val accessToken: String,
    val refreshToken: String,
)
