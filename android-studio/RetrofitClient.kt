package com.library.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // User Management Service
    val userApiService: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL_USER)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }
    
    // Book Catalog Service
    val bookApiService: BookApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL_BOOK)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApiService::class.java)
    }
    
    // Loan Management Service
    val loanApiService: LoanApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL_LOAN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoanApiService::class.java)
    }
    
    // Notification Service
    val notificationApiService: NotificationApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL_NOTIFICATIONS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationApiService::class.java)
    }
    
    // Reports Service
    val reportApiService: ReportApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL_REPORTS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReportApiService::class.java)
    }
}

// Interceptor para agregar token automáticamente
class AuthInterceptor(private val token: String) : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}





