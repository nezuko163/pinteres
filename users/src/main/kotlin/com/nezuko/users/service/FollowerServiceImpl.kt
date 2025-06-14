package com.nezuko.users.service

import com.nezuko.common.Paginated
import com.nezuko.users.data.entity.FollowUsersEntity
import com.nezuko.users.data.entity.UserProfileEntity
import com.nezuko.users.data.repository.FollowUsersRepository
import com.nezuko.users.data.repository.UserProfileRepository
import com.nezuko.users.exceptions.NotFound
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class FollowerServiceImpl(
    private val followUsersRepository: FollowUsersRepository,
    private val userProfileRepository: UserProfileRepository
) : FollowerService {
    private val log = KotlinLogging.logger(this::class.java.canonicalName)

    override fun getSubscribersUsersByIdPaginated(
        userId: UUID,
        pageSize: Int,
        pageNumber: Int
    ): Flow<Paginated<UserProfileEntity>> =
        getSubscribersIdByIdPaginated(userId, pageSize, pageNumber)
            .map { paginated ->
                val data = userProfileRepository.findUserProfilesByIdIn(
                    paginated.data
                )
                log.debug { "Get subscribers Profiles by user id: $userId - data: $data" }
                Paginated(
                    data = data,
                    hasNextPage = paginated.hasNextPage,
                    pageNumber = paginated.pageNumber,
                    pageSize = paginated.pageSize
                )
            }


    override fun getSubscribersIdByIdPaginated(
        userId: UUID,
        pageSize: Int,
        pageNumber: Int
    ): Flow<Paginated<UUID>> = flow {
        val totalCount = followUsersRepository.countSubscribers(userId)
        val offset = (pageNumber - 1) * pageSize
        val ids = followUsersRepository.findSubscribersByUserId(userId, pageSize, offset)
        log.debug { "Get subscribers by user id: $userId - res: $ids" }
        emit(
            Paginated(
                data = ids,
                hasNextPage = offset + ids.size < totalCount,
                pageNumber = pageNumber,
                pageSize = pageSize
            )
        )
    }

    override fun getFollowingsUsersByIdPaginated(
        userId: UUID,
        pageSize: Int,
        pageNumber: Int
    ): Flow<Paginated<UserProfileEntity>> =
        getFollowingIdByIdPaginated(userId, pageSize, pageNumber)
            .map { paginated ->
                val data = userProfileRepository.findUserProfilesByIdIn(
                    paginated.data
                )
                log.debug { "Get following Profiles by user id: $userId - data: $data" }
                Paginated(
                    data = data,
                    hasNextPage = paginated.hasNextPage,
                    pageNumber = paginated.pageNumber,
                    pageSize = paginated.pageSize
                )
            }

    override fun getFollowingIdByIdPaginated(
        userId: UUID,
        pageSize: Int,
        pageNumber: Int
    ): Flow<Paginated<UUID>> = flow {
        val totalCount = followUsersRepository.countFollowees(userId)
        val offset = (pageNumber - 1) * pageSize
        val ids = followUsersRepository.findFolloweesByUserId(userId, pageSize, offset)
        log.info { "Get folowees by user id: $userId - res: $ids" }
        emit(
            Paginated(
                data = ids,
                hasNextPage = offset + ids.size < totalCount,
                pageNumber = pageNumber,
                pageSize = pageSize
            )
        )
    }

    override suspend fun follow(userId: UUID, followeeId: UUID): Boolean {
        log.info { "follow user: $userId - followee: $followeeId" }
        if (!userProfileRepository.existsById(userId)) {
            throw NotFound("User-subscriber $userId not found")
        }
        if (!userProfileRepository.existsById(followeeId)) {
            throw NotFound("User-followee $followeeId not found")
        }

        followUsersRepository.save(
            FollowUsersEntity(subscriberId = userId, followeeId = followeeId)
        )
        return true
    }

    override suspend fun unfollow(userId: UUID, followeeId: UUID): Boolean {
        log.info { "follow user: $userId - followee: $followeeId" }
        if (!userProfileRepository.existsById(userId)) {
            throw NotFound("User-subscriber $userId not found")
        }
        if (!userProfileRepository.existsById(followeeId)) {
            throw NotFound("User-followee $followeeId not found")
        }

        val removed = followUsersRepository.removeByFolloweeIdAndSubscriberId(
            followeeId = followeeId,
            subscriberId = followeeId
        )
        return removed == 1L
    }
}