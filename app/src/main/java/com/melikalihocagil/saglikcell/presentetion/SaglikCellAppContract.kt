package com.melikalihocagil.saglikcell.presentetion

/**
 * Ana ekran için State, Event ve Effect tanımları (UDF).
 */
data class SaglikCellAppState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val token: String? = null
)

sealed interface SaglikCellAppEvent {
    data object Logout : SaglikCellAppEvent
}

sealed interface SaglikCellAppEffect {
    data class ShowSnackbar(val message: String) : SaglikCellAppEffect
    data object NavigateToLogin : SaglikCellAppEffect
}
