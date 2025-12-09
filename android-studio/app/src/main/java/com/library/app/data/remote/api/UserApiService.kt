package com.library.app.data.remote.api

import com.library.app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {

    @POST("api/users/register")
    suspend fun register(@Body registration: UserRegistrationDTO): Response<UserResponseDTO>

    @POST("api/users/login")
    suspend fun login(@Body login: UserLoginDTO): Response<LoginResponseDTO>

    @GET("api/users/validate-token")
    suspend fun validateToken(@Query("token") token: String): Response<TokenValidationDTO>

    @GET("api/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Long): Response<UserResponseDTO>

    @PUT("api/users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Long,
        @Body update: UserUpdateDTO
    ): Response<UserResponseDTO>

    @PATCH("api/users/{userId}/block")
    suspend fun blockUser(
        @Path("userId") userId: Long,
        @Body block: BlockUserDTO
    ): Response<UserResponseDTO>

    @PATCH("api/users/{userId}/role")
    suspend fun changeRole(
        @Path("userId") userId: Long,
        @Body role: ChangeRoleDTO
    ): Response<UserResponseDTO>

    @GET("api/users")
    suspend fun getAllUsers(): Response<List<UserResponseDTO>>

    @POST("api/users/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    @POST("api/users/bulk")
    suspend fun loadUsersBulk(@Body users: List<UserDTOs.UserRegistrationRequest>): Response<UserDTOs.BulkLoadResponse>
}


