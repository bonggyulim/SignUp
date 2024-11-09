package com.example.signup.presentation.model

import com.example.signup.domain.model.UserEntity

fun UserEntity.toUserModel(): UserModel {
    return UserModel(
        userEmail,
        userName,
    )
}

fun UserModel.toUserEntity(): UserEntity {
    return UserEntity(
        userEmail,
        userName
    )
}