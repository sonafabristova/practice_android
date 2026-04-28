package ci.nsu.moble.main.network

import ci.nsu.moble.main.auth.TokenManager
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

object RetrofitClient {

    private const val BASE_URL = "http://192.168.200.160:8080/api/"

    fun createApiService(tokenManager: TokenManager): ApiService {
        val client = ApiClient.createOkHttpClient(tokenManager)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}