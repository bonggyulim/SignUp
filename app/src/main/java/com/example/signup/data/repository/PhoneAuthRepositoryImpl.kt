package com.example.signup.data.repository

import com.example.signup.domain.repository.PhoneAuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PhoneAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : PhoneAuthRepository {

    private var storedVerificationId: String? = null
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private var codeSent = false

    private val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            codeSent = true
            storedVerificationId = verificationId
            forceResendingToken = token
        }
    }

    override suspend fun requestPhoneAuth(phoneNumber: String): Boolean {
        return suspendCoroutine { continuation ->
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callback)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

            // 3초 정도 기다린 후에 코드 전송 상태를 확인
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    continuation.resume(storedVerificationId != null)
                }
            }, 3000)
        }
    }

    override suspend fun verifyCode(code: String): Boolean {
        return suspendCoroutine { continuation ->
            val verificationId = storedVerificationId
            if (verificationId != null) {
                val credential = PhoneAuthProvider.getCredential(verificationId, code)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(true) // 인증 성공
                        } else {
                            continuation.resume(false) // 인증 실패
                        }
                    }
            } else {
                continuation.resume(false) // 저장된 verificationId가 없으면 실패 처리
            }
        }
    }
}