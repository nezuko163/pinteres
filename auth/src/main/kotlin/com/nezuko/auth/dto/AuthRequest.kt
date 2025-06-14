package com.nezuko.auth.dto

data class AuthRequest(
    val login: String,
    val password: String
)
