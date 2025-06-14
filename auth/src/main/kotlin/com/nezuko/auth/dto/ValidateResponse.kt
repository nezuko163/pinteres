package com.nezuko.auth.dto

import auth.Auth

data class ValidateResponse(
    val login: String,
    val roles: List<String>,
    val isValid: Boolean,
    val reason: String? = null

) {
    companion object {
        fun error(reason: String): ValidateResponse {
            return ValidateResponse("", emptyList(), false, reason)
        }
    }
}

