package com.nezuko.auth.service

import com.nezuko.auth.data.entity.UserAuth
import com.nezuko.auth.data.repository.UserRepository
import com.nezuko.auth.domain.model.Role
import com.nezuko.auth.dto.TokenResponse
import com.nezuko.auth.dto.ValidateResponse
import com.nezuko.auth.exceptions.UserAlreadyExistsException
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authManager: UserDetailsRepositoryReactiveAuthenticationManager,
    private val jwtService: JwtService,
) : AuthService {
    override fun auth(login: String, password: String): Mono<TokenResponse> {
        val a = UsernamePasswordAuthenticationToken(login, password)
        return authManager.authenticate(a)
            .map {
                val accessToken = jwtService.generateToken(
                    userDetails = it.principal as UserDetails,
                    role = Role.valueOf(it.authorities.first().authority.removePrefix("ROLE_")),
                )

                val refreshToken = jwtService.generateRefreshToken(
                    userDetails = it.principal as UserDetails
                )

                TokenResponse(accessToken, refreshToken)
            }
    }

    override fun register(login: String, password: String): Mono<TokenResponse> {
        return userRepository.findByUid(login)
            .flatMap<UserAuth> {
                throw UserAlreadyExistsException(login)
            }
            .switchIfEmpty(
                userRepository.save<UserAuth>(
                    UserAuth(
                        uid = login,
                        passwordHash = passwordEncoder.encode(password),
                        role = Role.USER.name
                    )
                )
            )
            .flatMap {
                auth(login, password)
            }
    }

    override fun validate(token: String): ValidateResponse {
        val res = jwtService.extractClaims(token)
        val role = res.get("role", String::class.java) ?: return ValidateResponse.error("Invalid role")

        return ValidateResponse(res.subject, listOf(role), true)
    }
}