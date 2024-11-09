package com.example.signup.data.repository

import android.util.Log
import com.example.signup.domain.repository.UserRepository
import com.example.signup.data.model.UserResponse
import com.example.signup.data.model.toUserEntity
import com.example.signup.data.model.toUserResponse
import com.example.signup.domain.model.SignInCredential
import com.example.signup.domain.model.UserEntity
import com.example.signup.presentation.model.UserModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : UserRepository {
    override suspend fun getCurrentUser() {
        try {
            auth.currentUser?.uid ?: throw Exception("User not login")

        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun signUp(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("User not login")
            fireStore.collection("userInfo")
                .document(currentUserUid)
                .set(UserResponse(email, userName = ""))
                .await()
        } catch (e: FirebaseAuthException) {
            throw e
        }
    }

    override suspend fun signIn(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("User not login")
            fireStore.collection("userInfo").document(currentUserUid).get().await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun withdrawalUser() {
        try {
            auth.currentUser?.delete()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun signInWithCredential(credential: SignInCredential) {
        try {
            val firebaseCredential = GoogleAuthProvider.getCredential(credential.token, null)
            auth.signInWithCredential(firebaseCredential).await()
        } catch (e: Exception) {
            throw e
        }
    }

}