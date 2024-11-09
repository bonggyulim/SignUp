package com.example.signup.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signup.UiState
import com.example.signup.domain.usecase.GetCurrentUserUseCase
import com.example.signup.domain.usecase.SignInUseCase
import com.example.signup.presentation.model.UserModel
import com.example.signup.presentation.model.toUserModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    private val _signInState = MutableStateFlow<UiState<UserModel>>(UiState.Loading)
    val signInState: StateFlow<UiState<UserModel>> = _signInState.asStateFlow()

    private val _currentUserState = MutableStateFlow<UiState<UserModel>>(UiState.Loading)
    val currentUserState: StateFlow<UiState<UserModel>> = _currentUserState.asStateFlow()

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUserState.value = UiState.Loading
            try {
                val userInfo = getCurrentUserUseCase()
                _currentUserState.value = UiState.Success(userInfo.toUserModel())
            } catch (e: Exception) {
                _currentUserState.value = UiState.Error("error")
            }

        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInState.value = UiState.Loading
            try {
                val userInfo = signInUseCase(email, password)
                _signInState.value = UiState.Success(userInfo.toUserModel())
            } catch (e: FirebaseAuthException) {
                _signInState.value = when (e.errorCode) {
                    "auth/user-not-found" -> UiState.Error("auth/user-not-found")
                    "auth/wrong-password" -> UiState.Error("auth/wrong-password")
                    "auth/network-request-failed"-> UiState.Error("auth/network-request-failed")
                    else -> UiState.Error("Signup failed: ${e.message}")
                }
            } catch (e: Exception) {
                _signInState.value = UiState.Error("error")
            }

        }
    }
}