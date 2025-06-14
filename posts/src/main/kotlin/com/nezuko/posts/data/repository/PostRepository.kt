package com.nezuko.posts.data.repository

import com.nezuko.posts.data.entity.PostEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface PostRepository : CoroutineCrudRepository<PostEntity, UUID> {
    fun findPostEntitiesByAuthorId(authorId: UUID): Flow<PostEntity>


    @Query(
        """
        SELECT * FROM  posts
        WHERE author_id = :authorId
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
    """
    )
    suspend fun findPostsByAuthorIdPaginated(authorId: UUID, limit: Int, offset: Int): List<PostEntity>

    @Query("SELECT COUNT(*) FROM posts WHERE author_id = :authorId")
    suspend fun countPosts(authorId: UUID): Int

    @Query("""
        SELECT * FROM posts
        WHERE tags @> ARRAY[:tags]
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun findPostsByTagsPaginated(tags: List<String>, limit: Int, offset: Int): List<PostEntity>

    @Query("""
        SELECT * FROM posts
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun findPostsByUserRecommendations(limit: Int, offset: Int): List<PostEntity>

    @Query("SELECT COUNT(*) FROM posts WHERE tags @> ARRAY[:tags]")
    suspend fun countPostsByTags(tags: List<String>): Int

    @Query("UPDATE posts SET posted_at = :postedTime WHERE id = :id")
    fun updatePostedTime(@Param("postedTime") postedTime: Instant, @Param("id") postId: UUID): Flow<Int>

    @Query("UPDATE posts SET content = :content WHERE id = :id")
    fun updateContent(@Param("content") content: String, @Param("id") postId: UUID): Flow<Int>
}