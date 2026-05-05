package ci.nsu.moble.main.network

import ci.nsu.moble.main.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()


        val token = tokenManager.getTokenSync()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")

        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            println("✅ Токен добавлен в заголовок: ${token.take(20)}...")
        } else {
            println("❌ Токен пустой! Заголовок Authorization НЕ добавлен")
        }

        return chain.proceed(requestBuilder.build())
    }
}