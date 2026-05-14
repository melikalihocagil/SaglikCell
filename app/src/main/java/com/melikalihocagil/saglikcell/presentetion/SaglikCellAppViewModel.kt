package com.melikalihocagil.saglikcell.presentetion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melikalihocagil.saglikcell.domain.manager.TokenManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Uygulamanın ana durumunu yöneten ViewModel.
 * TokenManager'ı Single Source of Truth olarak dinler.
 */
class SaglikCellAppViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaglikCellAppState())
    val uiState: StateFlow<SaglikCellAppState> = _uiState.asStateFlow()

    private val _effect = Channel<SaglikCellAppEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        observeToken()
    }

    /**
     * Token değişimlerini sürekli dinleyerek State'i günceller (SSoT).
     */
    private fun observeToken() {
        tokenManager.getAccessToken()
            .onEach { token ->
                _uiState.update { 
                    it.copy(
                        token = token,
                        isAuthenticated = token != null 
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: SaglikCellAppEvent) {
        when (event) {
            is SaglikCellAppEvent.Logout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            tokenManager.deleteTokens()
            _effect.send(SaglikCellAppEffect.NavigateToLogin)
        }
    }
}
