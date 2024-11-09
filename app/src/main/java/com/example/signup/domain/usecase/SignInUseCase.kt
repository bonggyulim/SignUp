package com.example.signup.domain.usecase

import com.example.signup.domain.repository.UserRepository
import com.example.signup.domain.model.UserEntity
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke (email: String, password: String): UserEntity {
        return userRepository.signIn(email, password)
    }
}