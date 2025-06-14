package com.nezuko.auth.service

import com.nezuko.auth.data.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomReactiveUserDetailsService(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {
    private val log: Logger = LoggerFactory.getLogger(CustomReactiveUserDetailsService::class.java)
    override fun findByUsername(uid: String): Mono<UserDetails> =
        userRepository.findByUid(uid)
            .map { entity ->
                log.info("Found user {}", entity)
                User.withUsername(entity.uid)
                    .password(entity.passwordHash)
                    .roles(entity.role.removePrefix("ROLE_"))
                    .build()
            }
}