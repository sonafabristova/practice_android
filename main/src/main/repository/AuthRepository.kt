package ci.nsu.moble.main.repository

import ci.nsu.moble.main.auth.TokenManager
import ci.nsu.moble.main.data.model.*
import ci.nsu.moble.main.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * Результат операции для использования в ViewModel
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    /**
     * Вход в систему
     */
    suspend fun login(login: String, password: String): ApiResult<LoginResponse> {
        return try {
            val request = LoginRequest(login, password)
            val response = apiService.login(request)
            // Сохраняем токен после успешного входа
            tokenManager.saveToken(response.token)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Ошибка входа")
        }
    }

    /**
     * Регистрация нового пользователя
     */
    suspend fun register(request: RegisterRequest): ApiResult<Unit> {
        return try {
            apiService.register(request)
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Ошибка регистрации")
        }
    }

    /**
     * Получить список пользователей (требует авторизации)
     */
    suspend fun getUsers(): ApiResult<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            ApiResult.Success(users)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Ошибка получения пользователей")
        }
    }

    /**
     * Получить список групп для выбора при регистрации
     */
    suspend fun getGroups(): ApiResult<List<GroupDto>> {
        return try {
            val groups = apiService.getGroups()
            ApiResult.Success(groups)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Ошибка получения групп")
        }
    }

    /**
     * Выход из системы (удаление токена)
     */
    suspend fun logout() {
        tokenManager.clearToken()
    }

    /**
     * Проверить, авторизован ли пользователь
     */
    suspend fun isAuthenticated(): Boolean {
        return tokenManager.isAuthenticated()
    }

    /**
     * Получить текущий токен (для отладки)
     */
    fun getToken(): Flow<String?> = tokenManager.getTokenFlow()
}