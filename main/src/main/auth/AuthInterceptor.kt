
package ci.nsu.moble.main.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Получаем исходный запрос
        val originalRequest = chain.request()

        // Строим новый запрос с заголовками
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")

        // Добавляем токен, если он есть
        val token = tokenManager.getTokenSync()
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        // Выполняем запрос
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}