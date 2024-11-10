package com.example.signup.domain.usecase

import com.example.signup.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<Boolean> {
        return userRepository.withdrawalUser()
    }

}