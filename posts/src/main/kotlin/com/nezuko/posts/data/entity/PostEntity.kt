package com.nezuko.posts.data.entity

import com.nezuko.common.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("posts")
data class PostEntity(
    @Id val id: UUID? = null,
    val authorId: UUID,
    val content: String = "",
    val tags: List<String> = emptyList(),
    val postedAt: Instant = Instant.now(),
    val status: PostStatus = PostStatus.UNDEFINED
): BaseEntity()
