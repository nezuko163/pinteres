package com.nezuko.users.service

import com.nezuko.users.data.entity.UserProfileEntity
import com.nezuko.users.data.repository.UserProfileRepository
import com.nezuko.users.exceptions.NotFound
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserProfileServiceImpl(
    private val userRepository: UserProfileRepository
) : UserProfileService {
    private val log = KotlinLogging.logger(this::class.java.canonicalName)
    override fun getUserProfileByUid(uid: UUID): Flow<UserProfileEntity> = flow {
        log.info { "Get user profile by id $uid" }
        val user = userRepository.findById(uid)
        log.info { "user = $user" }
        if (user == null) {
            throw NotFound("User with id $uid not found")
        }
        log.info { "Get user profile by user id $uid" }
        emit(user)
    }

    override fun getUserProfilesByUids(uids: Collection<UUID>): Flow<UserProfileEntity> = flow {
        log.info { "Get user profiles by uids $uids" }
        val users = userRepository.findUserProfilesByIdIn(uids)
        log.info { "Get user profiles by user ids $users" }
        emitAll(users.asFlow())
    }


    override fun getUserProfilesByUsername(username: String): Flow<UserProfileEntity> {
        log.info { "Get user profiles by username: $username" }
        return userRepository.findUserProfilesByUsernameEquals(username)
    }

    override fun updateUserProfile(userProfileEntity: UserProfileEntity): Flow<UserProfileEntity> =
        flow {
            log.info { "Update user profile entity: $userProfileEntity" }
            val exists = userRepository.existsById(userProfileEntity.id!!)
            log.info { "Update user profile entity exists: $exists" }
            if (!exists) {
                log.error { "User with id ${userProfileEntity.id} not found" }
                throw NotFound("User ${userProfileEntity.id} not found")
            }
            log.info { "Update user profile entity exists: $exists" }
            val user = userRepository.save(userProfileEntity)
            log.info { "Saved user profile: $user" }
            emit(user)
        }

    override fun deleteUserProfile(uid: UUID): Flow<Unit> = flow {
        log.info { "Delete user $uid" }
        emit(userRepository.deleteById(uid))
    }

    override fun createUserProfile(userProfileEntity: UserProfileEntity): Flow<UserProfileEntity> =
        flow {
            log.info { "Create user profile entity: $userProfileEntity" }
            val user = userRepository.save(userProfileEntity)
            log.info { "Create user profile entity: $user" }
            emit(user)
        }

    override fun existsUser(userId: UUID): Flow<Boolean> = flow {
        log.info { "Exists user $userId" }
        val res = userRepository.existsById(userId)
        log.info { "Exists user $userId exists: $res" }
        emit(res)
    }

}