package com.example.signup.domain.usecase

import com.example.signup.domain.repository.UserRepository
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() {
        return userRepository.withdrawalUser()
    }

}