package ci.nsu.moble.main.repository

import ci.nsu.moble.main.auth.TokenManager
import ci.nsu.moble.main.data.model.*
import ci.nsu.moble.main.network.ApiService

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun login(login: String, password: String): ApiResult<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            tokenManager.saveToken(response.token)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Ошибка входа")
        }
    }

    suspend fun register(request: RegisterRequest): ApiResult<Unit> {
        return try {
            apiService.register(request)
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Ошибка регистрации")
        }
    }

    suspend fun getUsers(): ApiResult<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            ApiResult.Success(users)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Ошибка получения пользователей")
        }
    }

    suspend fun logout(): ApiResult<Unit> {
        return try {
            tokenManager.clearToken()
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Ошибка выхода")
        }
    }
}