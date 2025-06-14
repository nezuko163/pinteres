package com.nezuko.posts.service

import com.nezuko.common.Paginated
import com.nezuko.common.emitPaginated
import com.nezuko.posts.data.entity.PostEntity
import com.nezuko.posts.data.repository.PostRepository
import com.nezuko.posts.dto.UpdatePostRequest
import com.nezuko.posts.exceptions.NotFound
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Service
import java.util.*

@Service
class PostServiceImpl(
    val postRepository: PostRepository,
) : PostService {
    override fun findPostById(id: UUID): Flow<PostEntity> = flow {
        val post = postRepository.findById(id) ?: throw NotFound("Post not found")
        emit(post)
    }

    override fun createPost(post: PostEntity): Flow<PostEntity> = flow {
        emit(postRepository.save(post))
    }

    override fun deletePost(postId: UUID): Flow<Unit> = flow {
        postRepository.deleteById(postId)
        emit(Unit)
    }

    override fun updatePost(updatePost: UpdatePostRequest): Flow<PostEntity> = flow {
        val existedPost = postRepository.findById(updatePost.id) ?: throw NotFound("Post not found")
        val newPost: PostEntity
        updatePost.apply {
            newPost = existedPost.copy(
                content = content ?: existedPost.content,
                tags = tags ?: existedPost.tags,
                postedAt = postedAt ?: existedPost.postedAt,
            )
        }
        emit(postRepository.save(newPost))
    }

    override fun getPostsByAuthorId(authorId: UUID, pageSize: Int, pageNumber: Int): Flow<Paginated<PostEntity>> =
        flow {
            val totalCount = postRepository.countPosts(authorId)
            val offset = (pageNumber - 1) * pageSize
            val data = postRepository.findPostsByAuthorIdPaginated(authorId, pageSize, offset)
            postRepository.findPostsByUserRecommendations(pageSize, offset)
            emitPaginated(data, offset, totalCount, pageSize, pageNumber)
        }

    override fun getPostsByTags(tags: List<String>, pageSize: Int, pageNumber: Int): Flow<Paginated<PostEntity>> =
        flow {
            val totalCount = postRepository.countPostsByTags(tags)
            val offset = (pageNumber - 1) * pageSize
            val data = postRepository.findPostsByTagsPaginated(tags, pageSize, offset)
            postRepository.findPostsByUserRecommendations(pageSize, offset)
            emitPaginated(data, offset, totalCount, pageSize, pageNumber)
        }

    override fun getPostsByRecommendationsForUser(
        userId: UUID,
        pageSize: Int,
        pageNumber: Int,
    ): Flow<Paginated<PostEntity>> = flow {
        val totalCount = postRepository.count().toInt()
        val offset = (pageNumber - 1) * pageSize
        val data = postRepository.findPostsByUserRecommendations(pageSize, offset)
        emitPaginated(data, offset, totalCount, pageSize, pageNumber)
    }


}