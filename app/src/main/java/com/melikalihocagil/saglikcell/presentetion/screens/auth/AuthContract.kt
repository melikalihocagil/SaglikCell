package com.melikalihocagil.saglikcell.presentetion.screens.auth

/**
 * AuthScreen için State, Event ve Effect tanımları (UDF).
 */
data class AuthState(
    val phoneNumber: String = "",
    val otpCode: String = "",
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val isOtpSent: Boolean = false,
    val errorMessage: String? = null
)

sealed interface AuthEvent {
    data class OnPhoneNumberChange(val number: String) : AuthEvent
    data class OnOtpChange(val code: String) : AuthEvent
    data object OnLoginClick : AuthEvent
    data object OnVerifyOtpClick : AuthEvent
}

sealed interface AuthEffect {
    data class NavigateToOtp(val phoneNumber: String) : AuthEffect
    data object NavigateToDashboard : AuthEffect
    data class ShowSnackbar(val message: String) : AuthEffect
}
