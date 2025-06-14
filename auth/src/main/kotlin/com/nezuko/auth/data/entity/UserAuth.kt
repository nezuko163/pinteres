package com.nezuko.auth.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("auth_users")
data class UserAuth(
    @Id val id: Long? = null,
    val uid: String,
    val passwordHash: String,
    val role: String
)