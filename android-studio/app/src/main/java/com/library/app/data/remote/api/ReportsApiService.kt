package com.library.app.data.remote.api

import com.library.app.data.remote.dto.DashboardStatisticsDTO
import retrofit2.Response
import retrofit2.http.GET

interface ReportsApiService {

    @GET("api/reports/dashboard")
    suspend fun getDashboardStatistics(): Response<DashboardStatisticsDTO>
}


