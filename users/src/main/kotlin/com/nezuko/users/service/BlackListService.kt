package com.nezuko.users.service

import com.nezuko.common.Paginated
import com.nezuko.users.data.entity.BlackListEntity
import java.util.UUID

interface BlackListService {
    suspend fun getBlackList(userId: UUID, pageSize: Int, pageNumber: Int): Paginated<BlackListEntity>
    suspend fun blockUser(userId: UUID, blackUserId: UUID): Boolean
    suspend fun unblockUser(userId: UUID, blackUserId: UUID): Boolean
}