package ci.nsu.moble.main.network

import ci.nsu.moble.main.data.LoginRequest
import ci.nsu.moble.main.data.LoginResponse
import ci.nsu.moble.main.data.UserDto
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("users")
    suspend fun getUsers(): List<UserDto>
}