package com.library.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTOs para sincronización
data class BookSyncDTO(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("isbn") val isbn: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("publisher") val publisher: String? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("coverUrl") val coverUrl: String? = null,
    @SerializedName("totalCopies") val totalCopies: Int = 1,
    @SerializedName("availableCopies") val availableCopies: Int? = null,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("featured") val featured: Boolean = false
)

data class BookSyncResponseDTO(
    @SerializedName("totalProcessed") val totalProcessed: Int,
    @SerializedName("created") val created: Int,
    @SerializedName("updated") val updated: Int,
    @SerializedName("skipped") val skipped: Int,
    @SerializedName("errors") val errors: Int,
    @SerializedName("createdBooks") val createdBooks: List<BookResponseDTO>,
    @SerializedName("updatedBooks") val updatedBooks: List<BookResponseDTO>,
    @SerializedName("errorMessages") val errorMessages: List<String>
)


