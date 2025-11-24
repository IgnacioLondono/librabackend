package com.library.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Request DTOs
data class UserRegistrationDTO(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone") val phone: String? = null
)

data class UserLoginDTO(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class UserUpdateDTO(
    @SerializedName("name") val name: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("profileImageUri") val profileImageUri: String? = null
)

data class BlockUserDTO(
    @SerializedName("blocked") val blocked: Boolean
)

data class ChangeRoleDTO(
    @SerializedName("role") val role: String // "USUARIO" o "ADMINISTRADOR"
)

// Response DTOs
data class UserResponseDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("role") val role: String,
    @SerializedName("status") val status: String,
    @SerializedName("profileImageUri") val profileImageUri: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class LoginResponseDTO(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserResponseDTO,
    @SerializedName("expiresIn") val expiresIn: Long
)

data class TokenValidationDTO(
    @SerializedName("token") val token: String,
    @SerializedName("valid") val valid: Boolean,
    @SerializedName("userId") val userId: Long?,
    @SerializedName("message") val message: String
)


