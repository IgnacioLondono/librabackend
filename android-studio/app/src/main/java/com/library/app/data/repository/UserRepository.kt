package com.library.app.data.repository

import com.library.app.data.remote.api.UserApiService
import com.library.app.data.remote.dto.*
import com.library.app.data.local.TokenManager
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val tokenManager: TokenManager
) {

    suspend fun register(registration: UserRegistrationDTO): Response<UserResponseDTO> {
        return userApiService.register(registration)
    }

    suspend fun login(email: String, password: String): Response<LoginResponseDTO> {
        val loginDTO = UserLoginDTO(email = email, password = password)
        val response = userApiService.login(loginDTO)
        
        if (response.isSuccessful) {
            response.body()?.let { loginResponse ->
                tokenManager.saveToken(loginResponse.token)
                tokenManager.saveUserInfo(loginResponse.user.id, loginResponse.user.email)
            }
        }
        
        return response
    }

    suspend fun logout() {
        val token = tokenManager.getTokenSync()
        token?.let {
            try {
                userApiService.logout("Bearer $it")
            } catch (e: Exception) {
                // Ignorar errores al hacer logout
            }
        }
        tokenManager.clearToken()
    }

    suspend fun getUserById(userId: Long): Response<UserResponseDTO> {
        return userApiService.getUserById(userId)
    }

    suspend fun updateUser(userId: Long, update: UserUpdateDTO): Response<UserResponseDTO> {
        return userApiService.updateUser(userId, update)
    }

    suspend fun getAllUsers(): Response<List<UserResponseDTO>> {
        return userApiService.getAllUsers()
    }

    suspend fun validateToken(token: String): Response<TokenValidationDTO> {
        return userApiService.validateToken(token)
    }

    fun isLoggedIn(): Boolean {
        return kotlinx.coroutines.runBlocking {
            tokenManager.getTokenSync() != null
        }
    }
}


