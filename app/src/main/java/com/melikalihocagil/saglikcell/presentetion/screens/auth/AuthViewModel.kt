package com.melikalihocagil.saglikcell.presentetion.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melikalihocagil.saglikcell.domain.manager.TokenManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Giriş yapma mantığını yöneten ViewModel.
 * UDF prensiplerini uygular.
 */
class AuthViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<AuthEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnPhoneNumberChange -> {
                val isEnabled = event.number.length == 10
                _uiState.update { 
                    it.copy(
                        phoneNumber = event.number,
                        isButtonEnabled = isEnabled 
                    )
                }
            }
            is AuthEvent.OnOtpChange -> {
                val isEnabled = event.code.length == 6
                _uiState.update { 
                    it.copy(
                        otpCode = event.code,
                        isButtonEnabled = isEnabled
                    )
                }
            }
            is AuthEvent.OnLoginClick -> sendOtp()
            is AuthEvent.OnVerifyOtpClick -> verifyOtp()
        }
    }

    private fun sendOtp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000) // OTP gönderme simülasyonu
            _uiState.update { 
                it.copy(
                    isLoading = false,
                    isOtpSent = true,
                    isButtonEnabled = false 
                ) 
            }
            _effect.send(AuthEffect.NavigateToOtp(_uiState.value.phoneNumber))
        }
    }

    private fun verifyOtp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000) // Doğrulama simülasyonu
            tokenManager.saveTokens(
                accessToken = "mock_access_token_123",
                refreshToken = "mock_refresh_token_456"
            )
            _uiState.update { it.copy(isLoading = false) }
            _effect.send(AuthEffect.NavigateToDashboard)
        }
    }
}
