package com.library.app.data.remote

import com.library.app.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = kotlinx.coroutines.runBlocking {
            tokenManager.getTokenSync()
        }

        val authenticatedRequest = if (token != null && !request.url.encodedPath.contains("/login") 
            && !request.url.encodedPath.contains("/register")) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(authenticatedRequest)
    }
}

