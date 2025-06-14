package com.nezuko.users.data.repository

import com.nezuko.users.data.entity.BlackListEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface BlackListRepository : CoroutineCrudRepository<BlackListEntity, Long> {

    @Query("""
        SELECT user_black_id FROM blacklist 
        WHERE user_id = :userId
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun findBlackListByIdPaginated(userId: UUID, limit: Int, offset: Int): List<BlackListEntity>

    @Query("SELECT COUNT(*) FROM blacklist WHERE user_id = :userId")
    suspend fun countBlacklistByUserId(userId: UUID): Long

    suspend fun deleteByUserIdAndUserBlackId(userId: UUID, blacklistId: UUID): Long
}