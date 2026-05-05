package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.auth.TokenManager
import ci.nsu.moble.main.data.LoginRequest
import ci.nsu.moble.main.data.UserDto
import ci.nsu.moble.main.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

data class UsersUiState(
    val users: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _usersState = MutableStateFlow(UsersUiState())
    val usersState: StateFlow<UsersUiState> = _usersState.asStateFlow()

    fun updateLogin(login: String) {
        _loginState.value = _loginState.value.copy(login = login, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _loginState.value = _loginState.value.copy(password = password, errorMessage = null)
    }

    fun login(onSuccess: () -> Unit) {
        val state = _loginState.value

        if (state.login.isBlank() || state.password.isBlank()) {
            _loginState.value = _loginState.value.copy(errorMessage = "Заполните все поля")
            return
        }

        _loginState.value = _loginState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getInstance(tokenManager).apiService
                val response = apiService.login(LoginRequest(state.login, state.password))

                // Сохраняем токен
                tokenManager.saveToken(response.token)
                println("✅ Токен сохранён: ${response.token.take(30)}...")

                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
                onSuccess()
            } catch (e: Exception) {
                println("❌ Ошибка входа: ${e.message}")
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Ошибка входа"
                )
            }
        }
    }

    fun resetLoginSuccess() {
        _loginState.value = _loginState.value.copy(isSuccess = false)
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = _usersState.value.copy(isLoading = true, errorMessage = null)

            // Проверяем, есть ли токен перед запросом
            val token = tokenManager.getToken()
            println("🔑 Токен перед запросом users: ${token?.take(30)}...")

            if (token.isNullOrBlank()) {
                println("❌ ТОКЕН ПУСТОЙ! Невозможно загрузить пользователей")
                _usersState.value = _usersState.value.copy(
                    isLoading = false,
                    errorMessage = "Не авторизован. Войдите заново."
                )
                return@launch
            }

            try {
                val apiService = RetrofitClient.getInstance(tokenManager).apiService
                val users = apiService.getUsers()
                println("✅ Загружено пользователей: ${users.size}")
                _usersState.value = _usersState.value.copy(
                    isLoading = false,
                    users = users,
                    errorMessage = null
                )
            } catch (e: Exception) {
                println("❌ Ошибка загрузки пользователей: ${e.message}")
                _usersState.value = _usersState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Ошибка загрузки пользователей"
                )
            }
        }
    }
}