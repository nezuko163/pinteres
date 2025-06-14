package com.nezuko.posts.dto

import java.time.Instant
import java.util.UUID


data class UpdatePostRequest(
    val id: UUID,
    val content: String? = null,
    val tags: List<String>? = null,
    val postedAt: Instant? = null
)
