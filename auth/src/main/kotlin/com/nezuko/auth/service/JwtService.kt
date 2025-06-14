package com.nezuko.auth.service

import com.nezuko.auth.domain.model.Role
import com.nezuko.auth.properties.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    private val jwtProperties: JwtProperties
) {
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secret))

    fun generateToken(
        userDetails: UserDetails,
        role: Role = Role.USER,
        extraClaims: Map<String, Any> = emptyMap()
    ): String {
        return generateToken(userDetails.username, role, extraClaims)
    }

    fun generateRefreshToken(
        userDetails: UserDetails,
    ): String {
        return generateToken(userDetails.username, timeValidMillis = jwtProperties.accessTokenExpiration)
    }

    fun generateToken(
        username: String,
        role: Role = Role.USER,
        extraClaims: Map<String, Any> = emptyMap(),
        timeValidMillis: Long = jwtProperties.accessTokenExpiration
    ): String {
        return Jwts.builder()
            .claims(extraClaims)
            .claim("role", role.name)
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + timeValidMillis))
            .signWith(secretKey)
            .compact()
    }

    private fun isTokenExpired(claims: Claims): Boolean {
        return claims.expiration.before(Date())
    }


    private fun extractUsernameFromClaims(claims: Claims): String {
        return claims.subject
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }


    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val claims = extractClaims(token)
        return extractUsernameFromClaims(claims) == userDetails.username
    }
}