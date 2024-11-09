package com.example.signup.data.model

import com.example.signup.domain.model.UserEntity

fun UserEntity.toUserResponse(): UserResponse {
    return UserResponse(
        userEmail,
        userName,
    )
}

fun UserResponse.toUserEntity(): UserEntity {
    return UserEntity(
        userEmail,
        userName
    )
}