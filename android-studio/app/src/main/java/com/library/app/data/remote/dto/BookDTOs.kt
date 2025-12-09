package com.library.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Request DTOs
data class BookCreateDTO(
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("isbn") val isbn: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("publisher") val publisher: String? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("coverUrl") val coverUrl: String? = null,
    @SerializedName("totalCopies") val totalCopies: Int = 1,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("featured") val featured: Boolean = false
)

data class BookUpdateDTO(
    @SerializedName("title") val title: String? = null,
    @SerializedName("author") val author: String? = null,
    @SerializedName("isbn") val isbn: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("publisher") val publisher: String? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("coverUrl") val coverUrl: String? = null,
    @SerializedName("totalCopies") val totalCopies: Int? = null,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("featured") val featured: Boolean? = null
)

// Response DTOs
data class BookResponseDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("isbn") val isbn: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("coverUrl") val coverUrl: String?,
    @SerializedName("status") val status: String,
    @SerializedName("totalCopies") val totalCopies: Int,
    @SerializedName("availableCopies") val availableCopies: Int,
    @SerializedName("price") val price: Double?,
    @SerializedName("featured") val featured: Boolean,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class BookAvailabilityDTO(
    @SerializedName("bookId") val bookId: Long,
    @SerializedName("available") val available: Boolean,
    @SerializedName("availableCopies") val availableCopies: Int,
    @SerializedName("totalCopies") val totalCopies: Int,
    @SerializedName("message") val message: String
)

data class BookStatisticsDTO(
    @SerializedName("totalBooks") val totalBooks: Long,
    @SerializedName("availableBooks") val availableBooks: Long,
    @SerializedName("loanedBooks") val loanedBooks: Long
)

data class SeedResponse(
    @SerializedName("totalProcessed") val totalProcessed: Int,
    @SerializedName("inserted") val inserted: Int,
    @SerializedName("alreadyExists") val alreadyExists: Int,
    @SerializedName("errors") val errors: Int,
    @SerializedName("message") val message: String
)
    @SerializedName("reservedBooks") val reservedBooks: Long,
    @SerializedName("totalCopies") val totalCopies: Long,
    @SerializedName("availableCopies") val availableCopies: Long
)

data class PageResponse<T>(
    @SerializedName("content") val content: List<T>,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("first") val first: Boolean,
    @SerializedName("last") val last: Boolean
)


