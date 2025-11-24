package com.example.uinavegacion.domain.usecase

import com.example.uinavegacion.data.remote.dto.BookApi
import com.example.uinavegacion.data.remote.dto.LoanApi
import com.example.uinavegacion.data.remote.dto.LoanDto

/**
 * Use Case de ejemplo para crear préstamos
 * Este es un ejemplo de cómo estructurar Use Cases para poder testearlos
 */
class CreateLoanUseCase(
    private val loanApi: LoanApi,
    private val bookApi: BookApi
) {
    
    suspend operator fun invoke(
        userId: Long,
        bookId: Long,
        token: String
    ): LoanDto? {
        // 1. Verificar disponibilidad del libro
        val availabilityResponse = bookApi.checkAvailability(bookId)
        
        if (!availabilityResponse.isSuccessful || 
            availabilityResponse.body()?.available != true) {
            return null // Libro no disponible
        }

        // 2. Crear préstamo
        val createLoanRequest = com.example.uinavegacion.data.remote.dto.CreateLoanRequestDto(
            userId = userId,
            bookId = bookId,
            loanDays = 14
        )

        val loanResponse = loanApi.createLoan(createLoanRequest, "Bearer $token")

        return if (loanResponse.isSuccessful) {
            loanResponse.body()
        } else {
            null
        }
    }
}


