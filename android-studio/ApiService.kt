package com.library.api

import retrofit2.Response
import retrofit2.http.*

// User Management API
interface UserApiService {
    @POST(ApiConfig.UserEndpoints.REGISTER)
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
    
    @POST(ApiConfig.UserEndpoints.LOGIN)
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST(ApiConfig.UserEndpoints.LOGOUT)
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>
    
    @GET(ApiConfig.UserEndpoints.GET_USER)
    suspend fun getUser(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<UserResponse>
    
    @PUT(ApiConfig.UserEndpoints.UPDATE_USER)
    suspend fun updateUser(
        @Path("userId") userId: Long,
        @Body request: UpdateUserRequest,
        @Header("Authorization") token: String
    ): Response<UserResponse>
    
    @POST(ApiConfig.UserEndpoints.VALIDATE_TOKEN)
    suspend fun validateToken(@Body request: TokenValidationRequest): Response<TokenValidationResponse>
}

// Book Catalog API
interface BookApiService {
    @GET(ApiConfig.BookEndpoints.GET_ALL_BOOKS)
    suspend fun getAllBooks(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "title",
        @Query("sortDir") sortDir: String = "ASC"
    ): Response<BookPageResponse>
    
    @GET(ApiConfig.BookEndpoints.GET_BOOK)
    suspend fun getBook(@Path("bookId") bookId: Long): Response<BookResponse>
    
    @GET(ApiConfig.BookEndpoints.SEARCH_BOOKS)
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<BookPageResponse>
    
    @GET(ApiConfig.BookEndpoints.CHECK_AVAILABILITY)
    suspend fun checkAvailability(@Path("bookId") bookId: Long): Response<BookAvailabilityResponse>
    
    @GET(ApiConfig.BookEndpoints.GET_FEATURED)
    suspend fun getFeaturedBooks(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<BookPageResponse>
    
    @GET(ApiConfig.BookEndpoints.GET_BY_CATEGORY)
    suspend fun getBooksByCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<BookPageResponse>
}

// Loan Management API
interface LoanApiService {
    @POST(ApiConfig.LoanEndpoints.CREATE_LOAN)
    suspend fun createLoan(
        @Body request: CreateLoanRequest,
        @Header("Authorization") token: String
    ): Response<LoanResponse>
    
    @GET(ApiConfig.LoanEndpoints.GET_USER_LOANS)
    suspend fun getUserLoans(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<List<LoanResponse>>
    
    @GET(ApiConfig.LoanEndpoints.GET_ACTIVE_LOANS)
    suspend fun getActiveLoans(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<List<LoanResponse>>
    
    @POST(ApiConfig.LoanEndpoints.RETURN_LOAN)
    suspend fun returnLoan(
        @Path("loanId") loanId: Long,
        @Header("Authorization") token: String
    ): Response<LoanResponse>
    
    @PATCH(ApiConfig.LoanEndpoints.EXTEND_LOAN)
    suspend fun extendLoan(
        @Path("loanId") loanId: Long,
        @Header("Authorization") token: String
    ): Response<LoanResponse>
}

// Notification API
interface NotificationApiService {
    @GET(ApiConfig.NotificationEndpoints.GET_USER_NOTIFICATIONS)
    suspend fun getUserNotifications(
        @Path("userId") userId: Long,
        @Query("unreadOnly") unreadOnly: Boolean = false,
        @Header("Authorization") token: String
    ): Response<List<NotificationResponse>>
    
    @GET(ApiConfig.NotificationEndpoints.GET_UNREAD_COUNT)
    suspend fun getUnreadCount(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<Long>
    
    @PATCH(ApiConfig.NotificationEndpoints.MARK_AS_READ)
    suspend fun markAsRead(
        @Path("notificationId") notificationId: Long,
        @Header("Authorization") token: String
    ): Response<NotificationResponse>
}

// Reports API
interface ReportApiService {
    @GET(ApiConfig.ReportEndpoints.GET_DASHBOARD)
    suspend fun getDashboardStatistics(
        @Header("Authorization") token: String
    ): Response<DashboardStatisticsResponse>
}





