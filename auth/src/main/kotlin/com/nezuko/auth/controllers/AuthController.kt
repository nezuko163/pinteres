package com.nezuko.auth.controllers

import com.nezuko.auth.dto.AuthRequest
import com.nezuko.auth.dto.TokenResponse
import com.nezuko.auth.dto.ValidateRequest
import com.nezuko.auth.dto.ValidateResponse
import com.nezuko.auth.service.AuthService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest): Mono<TokenResponse> {
        authRequest.apply {
            return authService.auth(login, password)
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody authRequest: AuthRequest): Mono<TokenResponse> {
        authRequest.apply {
            return authService.register(login, password)
        }
    }

    @PostMapping("/validate")
    fun validate(@RequestBody validateRequest: ValidateRequest): ValidateResponse {
        return authService.validate(validateRequest.accessToken)
    }
}