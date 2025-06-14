package com.nezuko.auth.dto

import auth.Auth

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
