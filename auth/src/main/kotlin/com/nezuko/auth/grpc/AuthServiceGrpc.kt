package com.nezuko.auth.grpc


import auth.Auth
import auth.AuthServiceGrpcKt
import com.nezuko.auth.service.AuthService
import kotlinx.coroutines.reactive.awaitFirst
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class AuthServiceGrpc(
    private val authService: AuthService,
) : AuthServiceGrpcKt.AuthServiceCoroutineImplBase() {
    override suspend fun login(request: Auth.AuthRequest): Auth.TokenResponse {
        return authService.auth(request.login, request.password)
            .map { (accessToken, refreshToken) ->
                Auth.TokenResponse.newBuilder()
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .build()
            }
            .awaitFirst()
    }

    override suspend fun register(request: Auth.AuthRequest): Auth.TokenResponse {
        return authService.register(request.login, request.password)
            .map { (accessToken, refreshToken) ->
                Auth.TokenResponse.newBuilder()
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .build()
            }
            .awaitFirst()
    }

    override suspend fun validate(request: Auth.ValidateRequest): Auth.ValidateResponse {
        val res = authService.validate(request.accessToken)
        val builder = Auth.ValidateResponse.newBuilder()
            .setIsValid(res.isValid)
            .setLogin(res.login)
            .setReason(res.reason ?: "")
            .addAllRoles(res.roles)
        return builder.build()
    }
}