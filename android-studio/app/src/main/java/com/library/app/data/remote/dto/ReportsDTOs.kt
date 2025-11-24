package com.library.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DashboardStatisticsDTO(
    @SerializedName("totalBooks") val totalBooks: Long,
    @SerializedName("totalUsers") val totalUsers: Long,
    @SerializedName("totalLoans") val totalLoans: Long,
    @SerializedName("activeLoans") val activeLoans: Long,
    @SerializedName("overdueLoans") val overdueLoans: Long,
    @SerializedName("availableBooks") val availableBooks: Long,
    @SerializedName("loanedBooks") val loanedBooks: Long,
    @SerializedName("revenue") val revenue: Double,
    @SerializedName("dateRange") val dateRange: String
)


