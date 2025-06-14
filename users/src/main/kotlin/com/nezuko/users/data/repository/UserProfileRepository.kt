package com.nezuko.users.data.repository

import com.nezuko.users.data.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserProfileRepository : CoroutineCrudRepository<UserProfileEntity, UUID> {
    fun findUserProfilesByUsernameEquals(username: String): Flow<UserProfileEntity>
    fun findUserProfilesByGenderEquals(gender: String): Flow<UserProfileEntity>

    suspend fun findUserProfilesByIdIn(ids: Collection<UUID>): List<UserProfileEntity>
}