package com.nezuko.users.data.entity

import com.nezuko.common.BaseEntityWithoutUpdatedAt
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("blacklist")
data class BlackListEntity(
    @Id val id: Long? = null,
    val userId: UUID,
    val userBlackId: UUID
) : BaseEntityWithoutUpdatedAt()

