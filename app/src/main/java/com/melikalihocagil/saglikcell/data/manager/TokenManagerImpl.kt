package com.melikalihocagil.saglikcell.data.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.melikalihocagil.saglikcell.domain.manager.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * TokenManager arayüzünün DataStore tabanlı implementasyonu.
 * RTR (Refresh Token Rotation) desteği ile tokenları güvenli saklar.
 */
class TokenManagerImpl(
    private val dataStore: DataStore<Preferences>
) : TokenManager {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    override fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
    }

    override fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun deleteTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
}
