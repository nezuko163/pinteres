package com.nezuko.posts.service

import com.nezuko.common.Paginated
import com.nezuko.posts.data.entity.PostEntity
import com.nezuko.posts.dto.UpdatePostRequest
import kotlinx.coroutines.flow.Flow
import java.util.*

interface PostService {
    fun findPostById(id: UUID): Flow<PostEntity>
    fun createPost(post: PostEntity): Flow<PostEntity>
    fun deletePost(postId: UUID): Flow<Unit>
    fun updatePost(updatePost: UpdatePostRequest): Flow<PostEntity>
    fun getPostsByAuthorId(authorId: UUID, pageSize: Int, pageNumber: Int): Flow<Paginated<PostEntity>>
    fun getPostsByTags(tags: List<String>, pageSize: Int, pageNumber: Int): Flow<Paginated<PostEntity>>
    fun getPostsByRecommendationsForUser(userId: UUID, pageSize: Int, pageNumber: Int): Flow<Paginated<PostEntity>>
}