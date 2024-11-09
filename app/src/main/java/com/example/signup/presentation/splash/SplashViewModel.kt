package com.example.signup.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signup.UiState
import com.example.signup.domain.model.SignInCredential
import com.example.signup.domain.usecase.GetCurrentUserUseCase
import com.example.signup.domain.usecase.SignInWithCredentialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase
): ViewModel() {
    private val _signInState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val signInState: StateFlow<UiState<Boolean>> = _signInState.asStateFlow()

    private val _currentUserState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val currentUserState: StateFlow<UiState<Boolean>> = _currentUserState.asStateFlow()

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUserState.value = UiState.Loading
            try {
                getCurrentUserUseCase()
                _currentUserState.value = UiState.Success(true)
            } catch (e: Exception) {
                _currentUserState.value = UiState.Error("error")
            }

        }
    }


    fun signInWithGoogle(token: String) {
        viewModelScope.launch {
            _signInState.value = UiState.Loading
            try {
                val credential = SignInCredential(token)
                signInWithCredentialUseCase(credential)
                _signInState.value = UiState.Success(true)
            } catch (e: Exception) {
                _signInState.value = UiState.Error(e.message.toString())
            }
        }
    }
}