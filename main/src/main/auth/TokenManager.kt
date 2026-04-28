package ci.nsu.moble.main.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

// Расширение для DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    /**
     * Сохранить токен
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    /**
     * Получить токен как Flow (для наблюдения, используется в AuthInterceptor)
     */
    fun getTokenFlow(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    /**
     * Получить токен (одноразово, без наблюдения)
     */
    suspend fun getToken(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.firstOrNull()
    }

    /**
     * Получить токен синхронно (для Interceptor, который не поддерживает suspend)
     */
    fun getTokenSync(): String? {
        return runBlocking { getToken() }
    }

    /**
     * Проверить, авторизован ли пользователь
     */
    suspend fun isAuthenticated(): Boolean {
        val token = getToken()
        return !token.isNullOrBlank()
    }

    /**
     * Удалить токен (при выходе из системы)
     */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}