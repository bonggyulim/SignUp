package com.example.signup.domain.repository

import com.example.signup.domain.model.SignInCredential
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser()
    suspend fun signUp(email: String, password: String)
    suspend fun signIn(email: String, password: String)
    suspend fun signOut()
    suspend fun withdrawalUser(): Flow<Boolean>
    suspend fun signInWithCredential(credential: SignInCredential)
}