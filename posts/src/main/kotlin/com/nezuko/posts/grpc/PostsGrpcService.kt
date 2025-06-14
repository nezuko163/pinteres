package com.nezuko.posts.grpc

import com.google.protobuf.Empty
import com.nezuko.posts.mapper.toEntity
import com.nezuko.posts.mapper.toProto
import com.nezuko.posts.mapper.toRequest
import com.nezuko.posts.service.PostService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import mu.KLogging
import net.devh.boot.grpc.server.service.GrpcService
import post.PostServiceGrpcKt
import post.PostsDomain
import post.PostsDto
import java.util.*

@GrpcService
class PostsGrpcService(
    private val postService: PostService,
) : PostServiceGrpcKt.PostServiceCoroutineImplBase() {
    private val log = KLogging().logger(PostsGrpcService::class.java.canonicalName)
    override suspend fun createPost(request: PostsDto.CreatePostRequest): PostsDomain.Post {
        log.info { "creating post request: $request" }
        return postService
            .createPost(request.toEntity())
            .onEach { log.info { "post created: $it" } }
            .first()
            .toProto()
    }

    override suspend fun updatePost(request: PostsDto.UpdatePostRequest): PostsDomain.Post {
        return postService
            .updatePost(request.toRequest())
            .single()
            .toProto()
    }

    override suspend fun deletePost(request: PostsDto.DeletePostRequest): Empty {
        return postService
            .deletePost(UUID.fromString(request.id))
            .map { Empty.getDefaultInstance() }
            .single()
    }

    override suspend fun getPost(request: PostsDto.GetPostRequest): PostsDomain.Post {
        return postService
            .findPostById(UUID.fromString(request.id))
            .single()
            .toProto()
    }

    override suspend fun getPostsByAuthor(request: PostsDto.GetPostsByAuthorRequest): PostsDto.ListPostsResponse {
        return postService
            .getPostsByAuthorId(
                authorId = UUID.fromString(request.authorId),
                pageSize = request.pageSize,
                pageNumber = request.pageNumber
            )
            .single()
            .toProto()
    }

    override suspend fun getPostsByTags(request: PostsDto.GetPostsByTagsRequest): PostsDto.ListPostsResponse {
        return postService
            .getPostsByTags(
                tags = request.tagsList,
                pageSize = request.pageSize,
                pageNumber = request.pageNumber
            )
            .single()
            .toProto()
    }

    override suspend fun getPostsIdsByRecommendations(request: PostsDto.GetGetPostsIdsByRecommendationsRequest): PostsDto.ListPostsResponse {
        return postService
            .getPostsByRecommendationsForUser(
                userId = UUID.fromString(request.userId),
                pageSize = request.pageSize,
                pageNumber = request.pageNumber
            )
            .single()
            .toProto()
    }
}