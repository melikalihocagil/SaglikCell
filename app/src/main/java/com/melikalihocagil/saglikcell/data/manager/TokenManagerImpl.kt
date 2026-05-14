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
 * Token verilerini güvenli (isteğe bağlı olarak şifrelenmiş) şekilde saklar.
 */
class TokenManagerImpl(
    private val dataStore: DataStore<Preferences>
) : TokenManager {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    override fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun deleteToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}
