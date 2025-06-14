package com.nezuko.auth.config

import com.nezuko.auth.service.CustomReactiveUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val userDetailsService: CustomReactiveUserDetailsService
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(): UserDetailsRepositoryReactiveAuthenticationManager {
        val authManager = UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)
        authManager.setPasswordEncoder(passwordEncoder())
        return authManager
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .csrf { it.disable() }
            .authorizeExchange {
                it.pathMatchers("/auth/**").permitAll()
            }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .build()

}