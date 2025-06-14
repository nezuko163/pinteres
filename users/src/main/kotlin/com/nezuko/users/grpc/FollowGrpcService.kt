package com.nezuko.users.grpc

import com.nezuko.users.service.FollowerService
import followee.FollowServiceGrpcKt
import followee.FollowingDto
import kotlinx.coroutines.flow.single
import net.devh.boot.grpc.server.service.GrpcService
import java.util.*

@GrpcService
class FollowGrpcService(
    private val service: FollowerService
) : FollowServiceGrpcKt.FollowServiceCoroutineImplBase() {
    override suspend fun follow(request: FollowingDto.FollowRequest): FollowingDto.FollowResponse {
        val a = service.follow(UUID.fromString(request.subscriberId), UUID.fromString(request.followeeId))
        return FollowingDto.FollowResponse.newBuilder().setSuccess(a).build()
    }

    override suspend fun unfollow(request: FollowingDto.FollowRequest): FollowingDto.FollowResponse {
        val a = service.unfollow(UUID.fromString(request.subscriberId), UUID.fromString(request.followeeId))
        return FollowingDto.FollowResponse.newBuilder().setSuccess(a).build()
    }

    override suspend fun getFollowees(request: FollowingDto.FolloweesRequest): FollowingDto.FolloweesResponse {
        val followees = service.getFollowingIdByIdPaginated(
            userId = UUID.fromString(request.me),
            pageSize = request.pageSize,
            pageNumber = request.pageNumber
        ).single()

        val builder = FollowingDto.FolloweesResponse.newBuilder()
        builder.addAllFollowees(followees.map { uid -> uid.toString() }.asIterable())
        builder.setMe(request.me)
        builder.setPageSize(request.pageSize)
        builder.setPageNumber(request.pageNumber)

        return builder.build()
    }

    override suspend fun getSubscribers(request: FollowingDto.SubscribersRequest): FollowingDto.SubscribersResponse {
        val followees = service.getSubscribersIdByIdPaginated(
            userId = UUID.fromString(request.me),
            pageSize = request.pageSize,
            pageNumber = request.pageNumber
        )
            .single()

        val builder = FollowingDto.SubscribersResponse.newBuilder()
        builder.addAllSubscribers(followees.map { uid -> uid.toString() }.asIterable())
        builder.setMe(request.me)
        builder.setPageSize(request.pageSize)
        builder.setPageNumber(request.pageNumber)

        return builder.build()
    }
}