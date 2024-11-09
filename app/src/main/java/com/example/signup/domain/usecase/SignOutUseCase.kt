package com.example.signup.domain.usecase

import com.example.signup.domain.repository.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() {
        return userRepository.signOut()
    }
}