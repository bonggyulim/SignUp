package com.example.signup.domain.usecase

import com.example.signup.domain.repository.UserRepository
import com.example.signup.domain.model.UserEntity
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke (userEntity: UserEntity, password: String): UserEntity {
        return userRepository.signUp(userEntity, password)
    }
}