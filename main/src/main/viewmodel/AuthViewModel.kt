package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.model.PersonDto
import ci.nsu.moble.main.data.model.RegisterRequest
import ci.nsu.moble.main.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // Состояние для экрана входа
    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    // Состояние для экрана регистрации
    private val _registerState = MutableStateFlow(RegisterUiState())
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    // Состояние для экрана пользователей
    private val _usersState = MutableStateFlow(UsersUiState())
    val usersState: StateFlow<UsersUiState> = _usersState.asStateFlow()



    fun updateLogin(login: String) {
        _loginState.update { it.copy(login = login, isLoginValid = login.isNotBlank()) }
    }

    fun updatePassword(password: String) {
        _loginState.update { it.copy(password = password, isPasswordValid = password.isNotBlank()) }
    }

    fun login() {
        val state = _loginState.value
        if (!state.isLoginValid || !state.isPasswordValid) {
            _loginState.update { it.copy(errorMessage = "Заполните все поля") }
            return
        }

        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.login(state.login, state.password)

            when (result) {
                is AuthRepository.ApiResult.Success -> {
                    _loginState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    }
                }
                is AuthRepository.ApiResult.Error -> {
                    _loginState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun resetLoginSuccess() {
        _loginState.update { it.copy(isSuccess = false) }
    }


    fun updateFirstName(value: String) {
        _registerState.update { it.copy(firstName = value) }
    }

    fun updateLastName(value: String) {
        _registerState.update { it.copy(lastName = value) }
    }

    fun updateMiddleName(value: String) {
        _registerState.update { it.copy(middleName = value) }
    }

    fun updateBirthDate(value: String) {
        _registerState.update { it.copy(birthDate = value) }
    }

    fun updateGender(gender: String) {
        _registerState.update { it.copy(gender = gender) }
    }

    fun updateGroupId(groupId: Int) {
        _registerState.update { it.copy(groupId = groupId) }
    }

    fun updateRegLogin(value: String) {
        _registerState.update { it.copy(login = value) }
    }

    fun updateRegPassword(value: String) {
        _registerState.update { it.copy(password = value) }
    }

    fun updateEmail(value: String) {
        _registerState.update { it.copy(email = value) }
    }

    fun updatePhoneNumber(value: String) {
        _registerState.update { it.copy(phoneNumber = value) }
    }

    fun loadGroups() {
        viewModelScope.launch {
            _registerState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.getGroups()

            when (result) {
                is AuthRepository.ApiResult.Success -> {
                    _registerState.update {
                        it.copy(
                            isLoading = false,
                            availableGroups = result.data,
                            errorMessage = null
                        )
                    }
                }
                is AuthRepository.ApiResult.Error -> {
                    _registerState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun register() {
        val state = _registerState.value

        // \валидация
        if (state.firstName.isBlank() || state.lastName.isBlank() ||
            state.birthDate.isBlank() || state.login.isBlank() ||
            state.password.isBlank() || state.email.isBlank() ||
            state.phoneNumber.isBlank() || state.groupId == 0) {
            _registerState.update { it.copy(errorMessage = "Заполните все обязательные поля") }
            return
        }

        val person = PersonDto(
            firstName = state.firstName,
            lastName = state.lastName,
            middleName = state.middleName.takeIf { it.isNotBlank() },
            birthDate = state.birthDate,
            gender = state.gender,
            groupId = state.groupId
        )

        val request = RegisterRequest(
            login = state.login,
            password = state.password,
            email = state.email,
            phoneNumber = state.phoneNumber,
            person = person
        )

        viewModelScope.launch {
            _registerState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.register(request)

            when (result) {
                is AuthRepository.ApiResult.Success -> {
                    _registerState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    }
                }
                is AuthRepository.ApiResult.Error -> {
                    _registerState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun resetRegisterSuccess() {
        _registerState.update { it.copy(isSuccess = false) }
    }



    fun loadUsers() {
        viewModelScope.launch {
            _usersState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.getUsers()

            when (result) {
                is AuthRepository.ApiResult.Success -> {
                    _usersState.update {
                        it.copy(
                            isLoading = false,
                            users = result.data,
                            errorMessage = null
                        )
                    }
                }
                is AuthRepository.ApiResult.Error -> {
                    _usersState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun clearError() {
        _loginState.update { it.copy(errorMessage = null) }
        _registerState.update { it.copy(errorMessage = null) }
        _usersState.update { it.copy(errorMessage = null) }
    }
}