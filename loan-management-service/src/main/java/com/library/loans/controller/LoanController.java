package com.library.loans.controller;

import com.library.loans.dto.*;
import com.library.loans.model.Loan;
import com.library.loans.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Tag(name = "Loan Management", description = "API para gestión de préstamos")
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @Operation(summary = "Crear préstamo", description = "Crea un nuevo préstamo de libro")
    public ResponseEntity<LoanResponseDTO> createLoan(
            @Valid @RequestBody LoanCreateDTO createDTO,
            HttpServletRequest request) {
        String token = extractToken(request);
        LoanResponseDTO loan = loanService.createLoan(createDTO, token);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/{loanId}")
    @Operation(summary = "Obtener préstamo", description = "Obtiene la información de un préstamo por ID")
    public ResponseEntity<LoanResponseDTO> getLoan(
            @Parameter(description = "ID del préstamo") @PathVariable Long loanId) {
        LoanResponseDTO loan = loanService.getLoanById(loanId);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Préstamos de usuario", description = "Obtiene todos los préstamos de un usuario")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByUserId(
            @Parameter(description = "ID del usuario") @PathVariable Long userId) {
        List<LoanResponseDTO> loans = loanService.getLoansByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/user/{userId}/status")
    @Operation(summary = "Préstamos de usuario por estado", description = "Obtiene préstamos de un usuario filtrados por estado")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByUserIdAndStatus(
            @Parameter(description = "ID del usuario") @PathVariable Long userId,
            @RequestParam Loan.Status status) {
        List<LoanResponseDTO> loans = loanService.getLoansByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/user/{userId}/active")
    @Operation(summary = "Préstamos activos de usuario", description = "Obtiene los préstamos activos de un usuario")
    public ResponseEntity<List<LoanResponseDTO>> getActiveLoansByUserId(
            @Parameter(description = "ID del usuario") @PathVariable Long userId) {
        List<LoanResponseDTO> loans = loanService.getActiveLoansByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/book/{bookId}")
    @Operation(summary = "Préstamos de un libro", description = "Obtiene todos los préstamos de un libro")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByBookId(
            @Parameter(description = "ID del libro") @PathVariable Long bookId) {
        List<LoanResponseDTO> loans = loanService.getLoansByBookId(bookId);
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/{loanId}/return")
    @Operation(summary = "Registrar devolución", description = "Registra la devolución de un préstamo")
    public ResponseEntity<LoanResponseDTO> returnLoan(
            @Parameter(description = "ID del préstamo") @PathVariable Long loanId) {
        LoanResponseDTO loan = loanService.returnLoan(loanId);
        return ResponseEntity.ok(loan);
    }

    @PatchMapping("/{loanId}/extend")
    @Operation(summary = "Extender préstamo", description = "Extiende la fecha de vencimiento de un préstamo")
    public ResponseEntity<LoanResponseDTO> extendLoan(
            @Parameter(description = "ID del préstamo") @PathVariable Long loanId) {
        LoanResponseDTO loan = loanService.extendLoan(loanId);
        return ResponseEntity.ok(loan);
    }

    @PatchMapping("/{loanId}/cancel")
    @Operation(summary = "Cancelar préstamo", description = "Cancela un préstamo activo")
    public ResponseEntity<LoanResponseDTO> cancelLoan(
            @Parameter(description = "ID del préstamo") @PathVariable Long loanId) {
        LoanResponseDTO loan = loanService.cancelLoan(loanId);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Préstamos vencidos", description = "Obtiene la lista de préstamos vencidos")
    public ResponseEntity<List<LoanResponseDTO>> getOverdueLoans() {
        List<LoanResponseDTO> loans = loanService.getOverdueLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{loanId}/fine")
    @Operation(summary = "Calcular multa", description = "Calcula la multa de un préstamo vencido")
    public ResponseEntity<FineCalculationDTO> calculateFine(
            @Parameter(description = "ID del préstamo") @PathVariable Long loanId) {
        FineCalculationDTO fine = loanService.calculateFine(loanId);
        return ResponseEntity.ok(fine);
    }

    @GetMapping("/{loanId}/history")
    @Operation(summary = "Historial del préstamo", description = "Obtiene el historial completo de un préstamo")
    public ResponseEntity<List<LoanHistoryResponseDTO>> getLoanHistory(
            @Parameter(description = "ID del préstamo") @PathVariable Long loanId) {
        List<LoanHistoryResponseDTO> history = loanService.getLoanHistory(loanId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/validate")
    @Operation(summary = "Validar creación de préstamo", description = "Valida si se puede crear un préstamo")
    public ResponseEntity<LoanValidationDTO> validateLoan(
            @Valid @RequestBody LoanCreateDTO createDTO,
            HttpServletRequest request) {
        String token = extractToken(request);
        LoanValidationDTO validation = loanService.validateLoanCreation(createDTO, token);
        return ResponseEntity.ok(validation);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}





