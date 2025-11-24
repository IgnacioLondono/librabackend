package com.library.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Request DTOs
data class NotificationCreateDTO(
    @SerializedName("userId") val userId: Long,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("priority") val priority: String? = "MEDIUM"
)

// Response DTOs
data class NotificationResponseDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("read") val read: Boolean,
    @SerializedName("priority") val priority: String,
    @SerializedName("createdAt") val createdAt: String
)

data class UnreadCountResponseDTO(
    @SerializedName("userId") val userId: Long,
    @SerializedName("unreadCount") val unreadCount: Long
)


