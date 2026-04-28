package ci.nsu.moble.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)