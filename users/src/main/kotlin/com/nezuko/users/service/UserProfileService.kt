package com.nezuko.users.service

import com.nezuko.users.data.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface UserProfileService {
    fun getUserProfileByUid(uid: UUID): Flow<UserProfileEntity>
    fun getUserProfilesByUids(uids: Collection<UUID>): Flow<UserProfileEntity>
    fun getUserProfilesByUsername(username: String): Flow<UserProfileEntity>
    fun updateUserProfile(userProfileEntity: UserProfileEntity): Flow<UserProfileEntity>
    fun deleteUserProfile(uid: UUID): Flow<Unit>
    fun createUserProfile(userProfileEntity: UserProfileEntity): Flow<UserProfileEntity>
    fun existsUser(userId: UUID): Flow<Boolean>
}