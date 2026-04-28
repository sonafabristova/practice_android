package ci.nsu.moble.main.viewmodel

import ci.nsu.moble.main.data.model.GroupDto
import ci.nsu.moble.main.data.model.UserDto

/**
 * Состояние экрана входа
 */
data class LoginUiState(
    val login: String = "",
    val password: String = "",
    val isLoginValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

/**
 * Состояние экрана регистрации
 */
data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val birthDate: String = "",
    val gender: String = "MALE",
    val groupId: Int = 0,
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val availableGroups: List<GroupDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

/**
 * Состояние экрана пользователей
 */
data class UsersUiState(
    val users: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)