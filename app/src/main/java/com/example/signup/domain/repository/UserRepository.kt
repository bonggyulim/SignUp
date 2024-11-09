package com.example.signup.domain.repository

import com.example.signup.domain.model.UserEntity

interface UserRepository {
    suspend fun getCurrentUser() : UserEntity
    suspend fun signUp(userEntity: UserEntity, password: String) : UserEntity
    suspend fun signIn(email: String, password: String): UserEntity
}