package com.example.signup.data.repository

import com.example.signup.domain.repository.UserRepository
import com.example.signup.data.model.UserResponse
import com.example.signup.data.model.toUserEntity
import com.example.signup.data.model.toUserResponse
import com.example.signup.domain.model.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : UserRepository {
    override suspend fun getCurrentUser(): UserEntity {
        return try {
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("User not login")
            val snapshot = fireStore.collection("userInfo")
                .document(currentUserUid)
                .get()
                .await()

            snapshot.toObject(UserResponse::class.java)?.toUserEntity() ?: throw Exception("Not Found User")

        } catch (e: Exception) {
            throw e
        }

    }

    override suspend fun signUp(userEntity: UserEntity, password: String): UserEntity {
        return try {
            auth.createUserWithEmailAndPassword(userEntity.userEmail, password).await()
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("User not login")
            fireStore.collection("userInfo")
                .document(currentUserUid)
                .set(userEntity.toUserResponse())
                .await()
            userEntity
        } catch (e: FirebaseAuthException) {
            throw e
        }
    }

    override suspend fun signIn(email: String, password: String): UserEntity {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("User not login")
            val snapshot = fireStore.collection("userInfo").document(currentUserUid).get().await()
            snapshot.toObject(UserResponse::class.java)?.toUserEntity() ?: throw Exception("Do not log in")
        } catch (e: FirebaseAuthException) {
            throw e
        }
    }
}