package com.nezuko.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant

open class BaseEntityWithoutUpdatedAt(
    @CreatedDate
    @Column("created_at")
    var createdAt: Instant? = null,
)