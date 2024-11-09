package com.example.signup.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signup.UiState
import com.example.signup.domain.usecase.SignUpUseCase
import com.example.signup.presentation.model.UserModel
import com.example.signup.presentation.model.toUserEntity
import com.example.signup.presentation.model.toUserModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
): ViewModel() {
    private val _signUpState = MutableStateFlow<UiState<UserModel>>(UiState.Loading)
    val signUpState: StateFlow<UiState<UserModel>> = _signUpState.asStateFlow()

    fun signUp(userModel: UserModel, password: String) {
        viewModelScope.launch {
            _signUpState.value = UiState.Loading
            try {
                val userInfo = signUpUseCase(userModel.toUserEntity(), password)
                _signUpState.value = UiState.Success(userInfo.toUserModel())
            } catch (e: FirebaseAuthException) {
                _signUpState.value = when (e.errorCode) {
                    "auth/email-already-in-use" -> UiState.Error("auth/email-already-in-use")
                    "auth/weak-password" -> UiState.Error("auth/weak-password")
                    "auth/network-request-failed" -> UiState.Error("auth/network-request-failed")
                    "auth/invalid-email" -> UiState.Error("auth/invalid-email")
                    else -> UiState.Error("Signup failed: ${e.message}")
                }
            } catch (e: Exception) {
                _signUpState.value = UiState.Error("error")
            }
        }
    }
}