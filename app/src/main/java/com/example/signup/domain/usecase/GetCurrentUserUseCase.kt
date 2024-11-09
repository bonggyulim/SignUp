package com.example.signup.domain.usecase
import com.example.signup.domain.repository.UserRepository
import com.example.signup.domain.model.UserEntity
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke () {
        return userRepository.getCurrentUser()
    }
}
