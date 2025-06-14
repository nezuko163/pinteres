package com.nezuko.common

import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant

open class BaseEntity(
    @LastModifiedDate
    @Column("updated_at")
    var updatedAt: Instant? = null,
) : BaseEntityWithoutUpdatedAt()