package com.nezuko.users.service

import com.nezuko.common.Paginated
import com.nezuko.users.data.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

interface FollowerService {
    fun getSubscribersUsersByIdPaginated(userId: UUID, pageSize: Int, pageNumber: Int): Flow<Paginated<UserProfileEntity>>
    fun getSubscribersIdByIdPaginated(userId: UUID, pageSize: Int, pageNumber: Int): Flow<Paginated<UUID>>
    fun getFollowingsUsersByIdPaginated(userId: UUID, pageSize: Int, pageNumber: Int): Flow<Paginated<UserProfileEntity>>
    fun getFollowingIdByIdPaginated(userId: UUID, pageSize: Int, pageNumber: Int): Flow<Paginated<UUID>>
    suspend fun follow(userId: UUID, followeeId: UUID): Boolean
    suspend fun unfollow(userId: UUID, followeeId: UUID): Boolean
}