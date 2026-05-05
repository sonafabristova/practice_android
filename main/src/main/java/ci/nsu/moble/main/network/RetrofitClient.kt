package ci.nsu.moble.main.network

import ci.nsu.moble.main.auth.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor(private val tokenManager: TokenManager) {

    companion object {
        private const val BASE_URL = "http://192.168.200.160:8080/api/"

        @Volatile
        private var instance: RetrofitClient? = null

        fun getInstance(tokenManager: TokenManager): RetrofitClient {
            return instance ?: synchronized(this) {
                instance ?: RetrofitClient(tokenManager).also { instance = it }
            }
        }
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AuthInterceptor(tokenManager))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}