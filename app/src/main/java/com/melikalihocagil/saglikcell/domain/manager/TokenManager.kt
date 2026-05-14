package com.melikalihocagil.saglikcell.domain.manager

import kotlinx.coroutines.flow.Flow

/**
 * JWT Token yönetimi için arayüz.
 * Single Source of Truth (SSoT) prensibi gereği tüm uygulama token bilgisini buradan alır.
 */
interface TokenManager {
    fun getToken(): Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun deleteToken()
}
