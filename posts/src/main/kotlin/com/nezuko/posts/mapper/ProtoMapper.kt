package com.nezuko.posts.mapper

import com.google.protobuf.Timestamp
import com.nezuko.common.Paginated
import com.nezuko.posts.data.entity.PostEntity
import com.nezuko.posts.data.entity.PostStatus

import com.nezuko.posts.dto.UpdatePostRequest
import mu.KotlinLogging
import post.PostsDomain
import post.PostsDto
import java.time.Instant
import java.util.*

object ProtoMapper

private val logger = KotlinLogging.logger(ProtoMapper::class.java.canonicalName)

fun PostEntity.toProto(): PostsDomain.Post {
    val builder = PostsDomain.Post.newBuilder()
        .setId(id.toString())
        .setAuthorId(authorId.toString())
        .setContent(content)
        .addAllTags(tags)
        .setPostedAt(postedAt.toProtoTimestamp())
        .setStatus(status.toProto())

    createdAt?.let { builder.createdAt = it.toProtoTimestamp() }
    updatedAt?.let { builder.updatedAt = it.toProtoTimestamp() }

    return builder.build()
}

fun PostsDomain.Post.toEntity(): PostEntity {
    val post =  PostEntity(
        id = UUID.fromString(this.id),
        authorId = UUID.fromString(this.authorId),
        content = this.content,
        tags = this.tagsList,
        postedAt = this.postedAt.toInstant(),
        updatedAt = if (hasUpdatedAt()) updatedAt.toInstant() else null,
        status = this.status.toEntity(),
    )
    post.createdAt = if (hasCreatedAt()) createdAt.toInstant() else null
    post.updatedAt = if (hasUpdatedAt()) updatedAt.toInstant() else null
}

// Enum mapping
fun PostStatus.toProto(): PostsDomain.PostStatus = when (this) {
    PostStatus.SCHEDULED -> PostsDomain.PostStatus.SCHEDULED
    PostStatus.PUBLISHED -> PostsDomain.PostStatus.PUBLISHED
    PostStatus.ARCHIVED -> PostsDomain.PostStatus.ARCHIVED
    PostStatus.UNDEFINED -> PostsDomain.PostStatus.UNDEFINED
}

fun PostsDomain.PostStatus.toEntity(): PostStatus = when (this) {
    PostsDomain.PostStatus.SCHEDULED -> PostStatus.SCHEDULED
    PostsDomain.PostStatus.PUBLISHED -> PostStatus.PUBLISHED
    PostsDomain.PostStatus.ARCHIVED -> PostStatus.ARCHIVED
    PostsDomain.PostStatus.UNDEFINED -> PostStatus.UNDEFINED
    PostsDomain.PostStatus.POST_STATUS_UNSPECIFIED -> PostStatus.UNDEFINED
    PostsDomain.PostStatus.UNRECOGNIZED -> PostStatus.UNDEFINED
}

fun PostsDto.ListPostsResponse.toList(): List<PostEntity> = this.postsList.map {
    it.toEntity()
}

fun Paginated<PostEntity>.toProto(): PostsDto.ListPostsResponse {
    val builder = PostsDto.ListPostsResponse.newBuilder()
    builder.addAllPosts(this.map { it.toProto() })
    return builder.build()
}

fun PostsDto.CreatePostRequest.toEntity(): PostEntity = PostEntity(
    authorId = UUID.fromString(authorId.apply { logger.info { "author - $this" } }),
    content = content,
    tags = tagsList,
    postedAt = postedAt.toInstant()
)

fun PostsDto.UpdatePostRequest.toRequest(): UpdatePostRequest = UpdatePostRequest(
    id = UUID.fromString(id),
    content = if (hasContent()) content!! else null,
    tags = if (hasTags()) tags!!.listList else null,
    postedAt = if (hasPostedAt()) postedAt!!.toInstant() else null,
)

// Instant ↔ Timestamp
fun Instant.toProtoTimestamp(): Timestamp =
    Timestamp.newBuilder()
        .setSeconds(this.epochSecond)
        .setNanos(this.nano)
        .build()

fun Timestamp.toInstant(): Instant =
    Instant.ofEpochSecond(this.seconds, this.nanos.toLong())

