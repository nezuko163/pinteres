package com.nezuko.common

import kotlinx.coroutines.flow.FlowCollector

data class Paginated<T>(
    val data: Collection<T>,
    val hasNextPage: Boolean,
    val pageNumber: Int,
    val pageSize: Int,
) : Iterable<T> {
    override fun iterator(): Iterator<T> {
        return data.iterator()
    }
}

suspend fun <T> FlowCollector<Paginated<T>>.emitPaginated(
    data: List<T>,
    offset: Int,
    totalCount: Int,
    pageSize: Int,
    pageNumber: Int
) {
    emit(
        Paginated<T>(
            data = data,
            hasNextPage = data.size + offset < totalCount,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    )
}