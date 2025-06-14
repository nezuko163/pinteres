package com.nezuko.users.mapper

import com.nezuko.users.data.entity.UserProfileEntity
import com.nezuko.users.domain.Gender
import user.UserDomain
import user.UserDto
import java.time.LocalDate
import java.util.*

fun UserDto.CreateUserProfileRequest.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        username = this.username,
        avatarId = if (hasAvatarId()) UUID.fromString(avatarId) else null,
        bio = if (hasBio()) bio else null,
        country = if (hasCountry()) country else null,
        birthDate = if (hasBirthDate()) LocalDate.parse(birthDate) else null,
        gender = if (hasGender()) gender.toModel() else null,
        language = if (hasLanguage()) language else null,
    )


}

fun UserDto.UpdateUserProfileRequest.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = UUID.fromString(id),
        username = this.username,
        avatarId = if (hasAvatarId()) UUID.fromString(avatarId) else null,
        bio = if (hasBio()) bio else null,
        country = if (hasCountry()) country else null,
        birthDate = if (hasBirthDate()) LocalDate.parse(birthDate) else null,
        gender = if (hasGender()) gender.toModel() else null,
        language = if (hasLanguage()) language else null,
    )
}



fun UserProfileEntity.toProto(): UserDomain.UserProfile {
    val a = this
    return UserDomain.UserProfile.newBuilder()
        .setId(id?.toString() ?: "")  // UUID -> String
        .setUsername(username ?: "")  // String? -> String
        .apply {
            // Опциональные поля (если null - не устанавливаем)
            a.avatarId?.let { setAvatarId(a.avatarId.toString()) }
            a.bio?.let { setBio(a.bio) }
            a.country?.let { setCountry(a.country) }
            a.birthDate?.let { setBirthDate(a.birthDate.toString()) }  // LocalDate -> ISO string
            a.gender?.let { setGender(a.gender.toProto()) }  // Enum conversion
            a.language?.let { setLanguage(a.language) }
        }
        .build()
}

fun UserDomain.Gender.toModel(): Gender = when (this) {
    UserDomain.Gender.MALE -> Gender.MALE
    UserDomain.Gender.FEMALE -> Gender.FEMALE
    UserDomain.Gender.OTHER -> Gender.OTHER
    UserDomain.Gender.UNSPECIFIED,
    UserDomain.Gender.UNRECOGNIZED -> Gender.UNSPECIFIED
}

fun Gender.toProto(): UserDomain.Gender = when (this) {
    Gender.MALE -> UserDomain.Gender.MALE
    Gender.FEMALE -> UserDomain.Gender.FEMALE
    Gender.OTHER -> UserDomain.Gender.OTHER
    Gender.UNSPECIFIED -> UserDomain.Gender.UNSPECIFIED
}
