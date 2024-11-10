package com.example.signup.presentation.phoneAuth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signup.UiState
import com.example.signup.domain.repository.PhoneAuthRepository
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val repository: PhoneAuthRepository
) : ViewModel() {

    private val _phoneAuthState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val phoneAuthState: StateFlow<UiState<Boolean>> = _phoneAuthState.asStateFlow()

    private val _codeAuthState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val codeAuthState: StateFlow<UiState<Boolean>> = _codeAuthState.asStateFlow()

    fun requestPhoneAuth(phoneNumber: String) {
        viewModelScope.launch {
            _phoneAuthState.value = UiState.Loading
            try {
                val codeSent = repository.requestPhoneAuth(phoneNumber)
                if (codeSent) {
                    _phoneAuthState.value = UiState.Success(true)
                } else {
                    _phoneAuthState.value = UiState.Error("Unknown Error")
                }
            } catch (e: FirebaseException) {
                _phoneAuthState.value = UiState.Error(e.message ?: "error")
            }
        }

    }

    fun verifyCode(code: String) {
        viewModelScope.launch {
            _codeAuthState.value = UiState.Loading
            val result = repository.verifyCode(code)
            if (result) {
                _codeAuthState.value = UiState.Success(true)
            } else {
                _codeAuthState.value = UiState.Error("Invalid verification code.")
            }
        }
    }
}
