package com.nezuko.users.data.repository

import com.nezuko.users.data.entity.FollowUsersEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FollowUsersRepository : CoroutineCrudRepository<FollowUsersEntity, Long> {

    @Query("""
        SELECT subscriber_id FROM user_follow 
        WHERE followee_id = :followeeId
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun findSubscribersByUserId(userId: UUID, limit: Int, offset: Int): List<UUID>

    @Query("""
        SELECT followee_id FROM user_follow 
        WHERE subscriber_id = :userId
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun findFolloweesByUserId(userId: UUID, limit: Int, offset: Int): List<UUID>

    @Query("""
        SELECT COUNT(*) FROM user_follow
        WHERE followee_id = :userId
    """)
    suspend fun countSubscribers(userId: UUID): Long


    @Query("""
        SELECT COUNT(*) FROM user_follow
        WHERE subscriber_id = :userId
    """)
    suspend fun countFollowees(userId: UUID): Long

    suspend fun removeByFolloweeIdAndSubscriberId(followeeId: UUID, subscriberId: UUID): Long

}