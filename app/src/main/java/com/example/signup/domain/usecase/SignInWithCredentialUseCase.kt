package com.example.signup.domain.usecase

import com.example.signup.domain.model.SignInCredential
import com.example.signup.domain.repository.UserRepository
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke (credential: SignInCredential) {
        return userRepository.signInWithCredential(credential)
    }
}
