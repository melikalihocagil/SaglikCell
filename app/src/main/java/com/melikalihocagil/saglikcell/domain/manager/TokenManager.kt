package com.melikalihocagil.saglikcell.domain.manager

import kotlinx.coroutines.flow.Flow

/**
 * JWT Token yönetimi için arayüz.
 * Access ve Refresh Token (RTR) desteği sağlar.
 */
interface TokenManager {
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun deleteTokens()
}
