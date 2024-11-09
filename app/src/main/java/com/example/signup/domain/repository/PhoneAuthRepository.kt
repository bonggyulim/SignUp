package com.example.signup.domain.repository

interface PhoneAuthRepository {
    suspend fun requestPhoneAuth(phoneNumber: String): Boolean
    suspend fun verifyCode(code: String): Boolean
}