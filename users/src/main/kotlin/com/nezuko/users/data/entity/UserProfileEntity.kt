package com.nezuko.users.data.entity

import com.nezuko.common.BaseEntity
import com.nezuko.users.domain.Gender
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.util.*

@Table("user_profiles")
data class UserProfileEntity(
    @Id val id: UUID? = null,
    val username: String?,
    val avatarId: UUID? = null,
    val bio: String? = null,
    val country: String? = null,
    val birthDate: LocalDate? = null,
    val gender: Gender? = null,
    val language: String? = null,
): BaseEntity()