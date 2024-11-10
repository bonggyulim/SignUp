package com.example.signup.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signup.UiState
import com.example.signup.domain.usecase.SignOutUseCase
import com.example.signup.domain.usecase.WithdrawalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val withdrawalUseCase: WithdrawalUseCase
): ViewModel() {
    private val _signOutState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val signOutState: StateFlow<UiState<Boolean>> = _signOutState.asStateFlow()

    private val _withdrawalState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val withdrawalState: StateFlow<UiState<Boolean>> = _withdrawalState.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            _signOutState.value = UiState.Loading
            try {
                signOutUseCase()
                _signOutState.value = UiState.Success(true)
            } catch (e: Exception) {
                _signOutState.value = UiState.Error(e.message ?: "error")
            }
        }
    }

    fun withdraw() {
        _withdrawalState.value = UiState.Loading

        viewModelScope.launch {
            withdrawalUseCase()
                .catch { e ->
                    _withdrawalState.value = UiState.Error(e.message ?: "Unknown error")
                }
                .collect { isSuccess ->
                    _withdrawalState.value = if (isSuccess) {
                        UiState.Success(true)
                    } else {
                        UiState.Error("회원 탈퇴 실패")
                    }
                }
        }
    }
}