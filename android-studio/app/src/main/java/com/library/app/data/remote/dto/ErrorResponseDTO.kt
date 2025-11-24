package com.library.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponseDTO(
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: String?,
    @SerializedName("message") val message: String,
    @SerializedName("path") val path: String?,
    @SerializedName("details") val details: List<String>? = null
)


