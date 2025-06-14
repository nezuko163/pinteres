package com.nezuko.users.data.entity

import com.nezuko.common.BaseEntityWithoutUpdatedAt
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("user_follow")
data class FollowUsersEntity(
    @Id val id: Long? = null,
    val subscriberId: UUID,
    val followeeId: UUID
): BaseEntityWithoutUpdatedAt()