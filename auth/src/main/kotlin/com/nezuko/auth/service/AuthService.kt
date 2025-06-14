package com.nezuko.auth.service

import com.nezuko.auth.dto.TokenResponse
import com.nezuko.auth.dto.ValidateResponse
import reactor.core.publisher.Mono

interface AuthService {
    fun auth(login: String, password: String): Mono<TokenResponse>
    fun register(login: String, password: String): Mono<TokenResponse>
    fun validate(token: String): ValidateResponse
}