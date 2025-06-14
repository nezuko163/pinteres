package com.nezuko.users.grpc

import com.nezuko.users.mapper.toEntity
import com.nezuko.users.mapper.toProto
import com.nezuko.users.service.FollowerService
import com.nezuko.users.service.UserProfileService
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import net.devh.boot.grpc.server.service.GrpcService
import user.UserDomain
import user.UserDto
import user.UserProfileServiceGrpcKt
import java.util.*

@GrpcService
class UserGrpcService(
    val userProfileService: UserProfileService,
    val followerService: FollowerService
) : UserProfileServiceGrpcKt.UserProfileServiceCoroutineImplBase() {
    private val log = KotlinLogging.logger(this::class.java.canonicalName)
    override suspend fun createUserProfile(request: UserDto.CreateUserProfileRequest): UserDomain.UserProfile {
        log.info { "creating user ${request.toEntity()}" }
        return userProfileService
            .createUserProfile(request.toEntity())
            .single()
            .toProto()
    }

    override suspend fun updateUserProfile(request: UserDto.UpdateUserProfileRequest): UserDomain.UserProfile {
        log.info { "updating user ${request.toEntity()}" }

        val a = userProfileService
            .updateUserProfile(request.toEntity())
            .single()

        log.info { "user = ${a}" }
        log.info { "user proto = ${a.toProto()}" }
        return a.toProto()
    }

    override suspend fun deleteUserProfile(request: UserDto.DeleteUserProfileRequest): UserDto.DeleteUserProfileResponse {
        return userProfileService
            .deleteUserProfile(UUID.fromString(request.id))
            .map { it -> UserDto.DeleteUserProfileResponse.newBuilder().setSuccess(true).build() }
            .single()
    }

    override suspend fun getUserProfile(request: UserDto.GetUserProfileRequest): UserDomain.UserProfile {
        val user = userProfileService
            .getUserProfileByUid(UUID.fromString(request.id))
            .single()
        log.info { "getting user - $user" }

        return user
            .toProto()
    }

    override suspend fun listUserProfiles(request: UserDto.ListUserProfilesRequest): UserDto.ListUserProfilesResponse {
        val res = UserDto.ListUserProfilesResponse.newBuilder()
        val list = userProfileService
            .getUserProfilesByUids(request.idsList.map { UUID.fromString(it) })
            .toList()

        log.info { "getting user profiles - $list" }
        list.forEach { i -> res.addProfiles(i.toProto()) }

        return res.build()
    }

}