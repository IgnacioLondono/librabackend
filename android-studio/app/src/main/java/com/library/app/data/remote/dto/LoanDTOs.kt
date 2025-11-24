package com.library.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Request DTOs
data class LoanCreateDTO(
    @SerializedName("userId") val userId: Long,
    @SerializedName("bookId") val bookId: Long,
    @SerializedName("loanDays") val loanDays: Int? = null
)

// Response DTOs
data class LoanResponseDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("bookId") val bookId: Long,
    @SerializedName("status") val status: String,
    @SerializedName("loanDate") val loanDate: String,
    @SerializedName("dueDate") val dueDate: String,
    @SerializedName("returnDate") val returnDate: String?,
    @SerializedName("extensions") val extensions: Int,
    @SerializedName("fine") val fine: Double?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class LoanValidationDTO(
    @SerializedName("userId") val userId: Long,
    @SerializedName("bookId") val bookId: Long,
    @SerializedName("valid") val valid: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("userExists") val userExists: Boolean?,
    @SerializedName("bookAvailable") val bookAvailable: Boolean?,
    @SerializedName("withinLoanLimit") val withinLoanLimit: Boolean?,
    @SerializedName("noActiveLoanForBook") val noActiveLoanForBook: Boolean?,
    @SerializedName("validLoanDays") val validLoanDays: Boolean?
)

data class FineCalculationDTO(
    @SerializedName("loanId") val loanId: Long,
    @SerializedName("daysOverdue") val daysOverdue: Int,
    @SerializedName("dailyFineRate") val dailyFineRate: Double,
    @SerializedName("totalFine") val totalFine: Double,
    @SerializedName("message") val message: String
)


