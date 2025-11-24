package com.library.api

import com.google.gson.annotations.SerializedName

// User Management Models
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: UserResponse,
    @SerializedName("expiresIn") val expiresIn: Long
)

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String?,
    val role: String,
    val status: String,
    @SerializedName("profileImageUri") val profileImageUri: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class UpdateUserRequest(
    val name: String? = null,
    val phone: String? = null,
    @SerializedName("profileImageUri") val profileImageUri: String? = null
)

data class TokenValidationRequest(
    val token: String
)

data class TokenValidationResponse(
    val token: String,
    val valid: Boolean,
    @SerializedName("userId") val userId: Long?,
    val message: String
)

// Book Catalog Models
data class BookResponse(
    val id: Long,
    val title: String,
    val author: String,
    val isbn: String?,
    val category: String?,
    val publisher: String?,
    val year: Int?,
    val description: String?,
    @SerializedName("coverUrl") val coverUrl: String?,
    val status: String,
    @SerializedName("totalCopies") val totalCopies: Int,
    @SerializedName("availableCopies") val availableCopies: Int,
    val price: Double?,
    val featured: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class BookPageResponse(
    val content: List<BookResponse>,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    val size: Int,
    val number: Int
)

data class BookAvailabilityResponse(
    @SerializedName("bookId") val bookId: Long,
    val available: Boolean,
    @SerializedName("availableCopies") val availableCopies: Int,
    @SerializedName("totalCopies") val totalCopies: Int,
    val message: String
)

// Loan Management Models
data class CreateLoanRequest(
    @SerializedName("userId") val userId: Long,
    @SerializedName("bookId") val bookId: Long,
    @SerializedName("loanDays") val loanDays: Int? = null
)

data class LoanResponse(
    val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("bookId") val bookId: Long,
    @SerializedName("loanDate") val loanDate: String,
    @SerializedName("dueDate") val dueDate: String,
    @SerializedName("returnDate") val returnDate: String?,
    val status: String,
    @SerializedName("loanDays") val loanDays: Int,
    @SerializedName("fineAmount") val fineAmount: Double,
    @SerializedName("extensionsCount") val extensionsCount: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String?
)

// Notification Models
data class NotificationResponse(
    val id: Long,
    @SerializedName("userId") val userId: Long,
    val type: String,
    val title: String,
    val message: String,
    val read: Boolean,
    val priority: String,
    @SerializedName("createdAt") val createdAt: String
)

// Reports Models
data class DashboardStatisticsResponse(
    @SerializedName("totalBooks") val totalBooks: Long,
    @SerializedName("totalUsers") val totalUsers: Long,
    @SerializedName("totalLoans") val totalLoans: Long,
    @SerializedName("activeLoans") val activeLoans: Long,
    @SerializedName("overdueLoans") val overdueLoans: Long,
    @SerializedName("availableBooks") val availableBooks: Long,
    @SerializedName("loanedBooks") val loanedBooks: Long,
    val revenue: Double,
    @SerializedName("dateRange") val dateRange: String
)





