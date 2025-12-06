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
    @Operation(
        summary = "Crear préstamo", 
        description = "Crea un nuevo préstamo de libro. Valida las siguientes reglas de negocio: " +
                     "1) El usuario debe existir y estar activo en la BD, 2) El libro debe tener copias disponibles (disponibles > 0), " +
                     "3) El usuario no puede tener más de 5 préstamos activos, 4) El usuario no puede tener un préstamo activo del mismo libro, " +
                     "5) Los días de préstamo deben estar entre 7 y 30 días. Si todo es válido, reduce las copias disponibles del libro en 1 " +
                     "y crea una notificación. Requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Préstamo creado exitosamente",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoanResponseDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400", 
                description = "Validación fallida: usuario inválido, libro no disponible, límite de préstamos excedido, o días inválidos"
            )
        }
    )
    public ResponseEntity<LoanResponseDTO> createLoan(
            @Parameter(description = "Datos del préstamo a crear (userId, bookId, loanDays opcional)", required = true)
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
    @Operation(
        summary = "Préstamos activos de usuario", 
        description = "Obtiene todos los préstamos activos (estado ACTIVE o OVERDUE) de un usuario específico. " +
                     "Útil para verificar si un usuario puede crear nuevos préstamos (límite de 5). " +
                     "Requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Lista de préstamos activos obtenida exitosamente"
            )
        }
    )
    public ResponseEntity<List<LoanResponseDTO>> getActiveLoansByUserId(
            @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long userId) {
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
    @Operation(
        summary = "Registrar devolución", 
        description = "Registra la devolución de un préstamo. Solo se pueden devolver préstamos con estado ACTIVE o OVERDUE. " +
                     "Al devolver: 1) Se establece la fecha de devolución, 2) Se cambia el estado a RETURNED, " +
                     "3) Se aumenta las copias disponibles del libro en 1, 4) Se calcula la multa si el préstamo está vencido, " +
                     "5) Se crea una notificación. Requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Préstamo devuelto exitosamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400", 
                description = "El préstamo no está activo o ya fue devuelto"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404", 
                description = "Préstamo no encontrado"
            )
        }
    )
    public ResponseEntity<LoanResponseDTO> returnLoan(
            @Parameter(description = "ID del préstamo a devolver", example = "1", required = true) @PathVariable Long loanId) {
        LoanResponseDTO loan = loanService.returnLoan(loanId);
        return ResponseEntity.ok(loan);
    }

    @PatchMapping("/{loanId}/extend")
    @Operation(
        summary = "Extender préstamo", 
        description = "Extiende la fecha de vencimiento de un préstamo por 7 días adicionales. Reglas: " +
                     "1) Solo se pueden extender préstamos con estado ACTIVE, 2) No se puede extender un préstamo vencido, " +
                     "3) Máximo 2 extensiones por préstamo. Al extender, se actualiza la fecha de vencimiento y se incrementa el contador de extensiones. " +
                     "Requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Préstamo extendido exitosamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400", 
                description = "No se puede extender: préstamo no activo, vencido, o ya tiene 2 extensiones"
            )
        }
    )
    public ResponseEntity<LoanResponseDTO> extendLoan(
            @Parameter(description = "ID del préstamo a extender", example = "1", required = true) @PathVariable Long loanId) {
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
    @Operation(
        summary = "Préstamos vencidos", 
        description = "Obtiene la lista de todos los préstamos vencidos (fecha de vencimiento anterior a hoy y estado ACTIVE). " +
                     "Automáticamente marca los préstamos como OVERDUE y calcula las multas correspondientes. " +
                     "Requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Lista de préstamos vencidos obtenida exitosamente"
            )
        }
    )
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
    @Operation(
        summary = "Validar creación de préstamo", 
        description = "Valida si se puede crear un préstamo sin crearlo realmente. Retorna información detallada sobre cada validación: " +
                     "usuario existe, libro disponible, dentro del límite de préstamos, no tiene préstamo activo del mismo libro, " +
                     "y días de préstamo válidos. Útil para mostrar mensajes de error específicos antes de intentar crear el préstamo. " +
                     "Requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Validación completada",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoanValidationDTO.class)
                )
            )
        }
    )
    public ResponseEntity<LoanValidationDTO> validateLoan(
            @Parameter(description = "Datos del préstamo a validar", required = true)
            @Valid @RequestBody LoanCreateDTO createDTO,
            HttpServletRequest request) {
        String token = extractToken(request);
        LoanValidationDTO validation = loanService.validateLoanCreation(createDTO, token);
        return ResponseEntity.ok(validation);
    }

    @GetMapping("/business-rules/validate")
    @Operation(
        summary = "Validar reglas de negocio", 
        description = "Valida todas las reglas de negocio implementadas en el sistema de préstamos. " +
                     "Retorna un resumen de 10 reglas principales: límite de préstamos activos, préstamo único por libro, " +
                     "rango de días de préstamo, verificación de disponibilidad, validación de usuario, actualización de copias, " +
                     "extensión de préstamos, cálculo de multas, no extender vencidos, y validación de devolución. " +
                     "Útil para testing y verificación del correcto funcionamiento del sistema. No requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Validación de reglas completada",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = BusinessRulesValidationDTO.class)
                )
            )
        }
    )
    public ResponseEntity<BusinessRulesValidationDTO> validateBusinessRules() {
        BusinessRulesValidationDTO validation = loanService.validateAllBusinessRules();
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





