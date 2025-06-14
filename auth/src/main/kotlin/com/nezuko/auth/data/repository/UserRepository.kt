package com.nezuko.auth.data.repository

import com.nezuko.auth.data.entity.UserAuth
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : R2dbcRepository<UserAuth, Long> {
    fun findByUid(uid: String): Mono<UserAuth>
}