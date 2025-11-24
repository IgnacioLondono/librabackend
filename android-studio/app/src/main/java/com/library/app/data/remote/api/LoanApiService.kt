package com.library.app.data.remote.api

import com.library.app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface LoanApiService {

    @POST("api/loans")
    suspend fun createLoan(@Body loan: LoanCreateDTO): Response<LoanResponseDTO>

    @GET("api/loans/{loanId}")
    suspend fun getLoanById(@Path("loanId") loanId: Long): Response<LoanResponseDTO>

    @GET("api/loans/user/{userId}")
    suspend fun getLoansByUserId(@Path("userId") userId: Long): Response<List<LoanResponseDTO>>

    @GET("api/loans/user/{userId}/active")
    suspend fun getActiveLoansByUserId(@Path("userId") userId: Long): Response<List<LoanResponseDTO>>

    @GET("api/loans/overdue")
    suspend fun getOverdueLoans(): Response<List<LoanResponseDTO>>

    @POST("api/loans/{loanId}/return")
    suspend fun returnLoan(@Path("loanId") loanId: Long): Response<LoanResponseDTO>

    @POST("api/loans/{loanId}/extend")
    suspend fun extendLoan(@Path("loanId") loanId: Long): Response<LoanResponseDTO>

    @DELETE("api/loans/{loanId}")
    suspend fun cancelLoan(@Path("loanId") loanId: Long): Response<LoanResponseDTO>

    @GET("api/loans/{loanId}/fine")
    suspend fun calculateFine(@Path("loanId") loanId: Long): Response<FineCalculationDTO>

    @POST("api/loans/validate")
    suspend fun validateLoan(@Body loan: LoanCreateDTO): Response<LoanValidationDTO>
}


