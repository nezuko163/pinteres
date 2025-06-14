package com.nezuko.users.service

import com.nezuko.common.Paginated
import com.nezuko.users.data.entity.BlackListEntity
import com.nezuko.users.data.repository.BlackListRepository
import com.nezuko.users.data.repository.UserProfileRepository
import com.nezuko.users.exceptions.NotFound
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BlackListServiceImpl(
    private val blackListRepository: BlackListRepository,
    private val userprofileRepository: UserProfileRepository
) : BlackListService {
    override suspend fun getBlackList(
        userId: UUID,
        pageSize: Int,
        pageNumber: Int
    ): Paginated<BlackListEntity> {
        val totalCount = blackListRepository.countBlacklistByUserId(userId)
        val offset = (pageNumber - 1) * pageSize
        val data = blackListRepository.findBlackListByIdPaginated(userId, pageSize, offset)
        return Paginated(
            data = data,
            hasNextPage = offset + data.size < totalCount,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    override suspend fun blockUser(userId: UUID, blackUserId: UUID): Boolean {
        if (!userprofileRepository.existsById(userId)) {
            throw NotFound("User $userId - user - not found")
        }
        if (!userprofileRepository.existsById(blackUserId)) {
            throw NotFound("User $blackUserId - blacklist user - user not found")
        }
        blackListRepository.save(BlackListEntity(userId = userId, userBlackId = blackUserId))
        return true
    }

    override suspend fun unblockUser(userId: UUID, blackUserId: UUID): Boolean {
        if (!userprofileRepository.existsById(userId)) {
            throw NotFound("User $userId - user - not found")
        }
        if (!userprofileRepository.existsById(blackUserId)) {
            throw NotFound("User $blackUserId - blacklist user - user not found")
        }
        val rows = blackListRepository.deleteByUserIdAndUserBlackId(userId, blackUserId)
        return rows == 1L
    }
}