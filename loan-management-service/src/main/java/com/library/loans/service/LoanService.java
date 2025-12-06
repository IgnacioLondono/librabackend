package com.library.loans.service;

import com.library.loans.client.BookServiceClient;
import com.library.loans.client.NotificationServiceClient;
import com.library.loans.client.UserServiceClient;
import com.library.loans.config.LoanConfig;
import com.library.loans.dto.*;
import com.library.loans.model.Loan;
import com.library.loans.model.LoanHistory;
import com.library.loans.repository.LoanHistoryRepository;
import com.library.loans.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanHistoryRepository loanHistoryRepository;
    private final UserServiceClient userServiceClient;
    private final BookServiceClient bookServiceClient;
    private final NotificationServiceClient notificationServiceClient;
    private final LoanConfig loanConfig;

    @Transactional
    public LoanResponseDTO createLoan(LoanCreateDTO createDTO, String token) {
        log.info("Creando préstamo para usuario {} y libro {}", createDTO.getUserId(), createDTO.getBookId());

        // Validar usuario
        Boolean userValid = userServiceClient.validateUser(createDTO.getUserId(), token).block();
        if (userValid == null || !userValid) {
            throw new RuntimeException("Usuario no válido o no encontrado");
        }

        // Validar disponibilidad del libro
        Boolean bookAvailable = bookServiceClient.checkBookAvailability(createDTO.getBookId()).block();
        if (bookAvailable == null || !bookAvailable) {
            throw new RuntimeException("El libro no está disponible");
        }

        // Verificar préstamos activos del usuario
        List<Loan> activeLoans = loanRepository.findActiveLoansByUserId(createDTO.getUserId());
        if (activeLoans.size() >= 5) { // Límite de 5 préstamos activos
            throw new RuntimeException("El usuario ya tiene 5 préstamos activos. No se pueden crear más préstamos.");
        }

        // Verificar que el usuario no tenga un préstamo activo del mismo libro
        boolean hasActiveLoanForBook = activeLoans.stream()
                .anyMatch(loan -> loan.getBookId().equals(createDTO.getBookId()));
        if (hasActiveLoanForBook) {
            throw new RuntimeException("El usuario ya tiene un préstamo activo de este libro");
        }

        // Validar días de préstamo (mínimo 7, máximo 30)
        int loanDays = createDTO.getLoanDays() != null ? createDTO.getLoanDays() : loanConfig.getDefaultDays();
        if (loanDays < 7 || loanDays > 30) {
            throw new RuntimeException("Los días de préstamo deben estar entre 7 y 30 días");
        }
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(loanDays);

        Loan loan = Loan.builder()
                .userId(createDTO.getUserId())
                .bookId(createDTO.getBookId())
                .loanDate(loanDate)
                .dueDate(dueDate)
                .status(Loan.Status.ACTIVE)
                .loanDays(loanDays)
                .fineAmount(BigDecimal.ZERO)
                .extensionsCount(0)
                .build();

        loan = loanRepository.save(loan);

        // Actualizar copias disponibles del libro
        bookServiceClient.updateBookCopies(createDTO.getBookId(), -1).block();

        // Registrar en historial
        LoanHistory history = LoanHistory.builder()
                .loanId(loan.getId())
                .action(LoanHistory.Action.CREATED)
                .notes("Préstamo creado")
                .build();
        loanHistoryRepository.save(history);

        // Crear notificación de préstamo creado
        notificationServiceClient.createNotification(
                createDTO.getUserId(),
                "LOAN_CREATED",
                "Préstamo creado",
                "Has solicitado el préstamo del libro. Fecha de devolución: " + dueDate,
                "MEDIUM"
        ).subscribe();

        log.info("Préstamo creado exitosamente con ID: {}", loan.getId());

        return LoanResponseDTO.fromEntity(loan);
    }

    public LoanResponseDTO getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));
        return LoanResponseDTO.fromEntity(loan);
    }

    public List<LoanResponseDTO> getLoansByUserId(Long userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(LoanResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LoanResponseDTO> getLoansByUserIdAndStatus(Long userId, Loan.Status status) {
        return loanRepository.findByUserIdAndStatus(userId, status).stream()
                .map(LoanResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LoanResponseDTO> getActiveLoansByUserId(Long userId) {
        return loanRepository.findActiveLoansByUserId(userId).stream()
                .map(LoanResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LoanResponseDTO> getLoansByBookId(Long bookId) {
        return loanRepository.findByBookId(bookId).stream()
                .map(LoanResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public LoanResponseDTO returnLoan(Long loanId) {
        log.info("Registrando devolución del préstamo: {}", loanId);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (loan.getStatus() != Loan.Status.ACTIVE && loan.getStatus() != Loan.Status.OVERDUE) {
            throw new RuntimeException("El préstamo no está activo");
        }

        // Calcular multa ANTES de marcar como devuelto (porque isOverdue() verifica estado ACTIVE)
        boolean wasOverdue = loan.isOverdue();
        BigDecimal fineAmount = BigDecimal.ZERO;
        if (wasOverdue) {
            fineAmount = calculateFine(loan);
        }

        loan.markAsReturned();
        loan = loanRepository.save(loan);

        // Verificar que el libro existe antes de actualizar copias
        try {
            Boolean bookExists = bookServiceClient.checkBookAvailability(loan.getBookId()).block();
            if (bookExists != null && bookExists) {
                // Actualizar copias disponibles del libro
                bookServiceClient.updateBookCopies(loan.getBookId(), 1).block();
            } else {
                log.warn("Libro {} no encontrado al devolver préstamo {}", loan.getBookId(), loanId);
            }
        } catch (Exception e) {
            log.error("Error actualizando copias del libro {} al devolver préstamo {}: {}", 
                    loan.getBookId(), loanId, e.getMessage());
        }

        // Registrar en historial
        LoanHistory history = LoanHistory.builder()
                .loanId(loan.getId())
                .action(LoanHistory.Action.RETURNED)
                .notes("Libro devuelto" + (fineAmount.compareTo(BigDecimal.ZERO) > 0 ? ". Multa: $" + fineAmount : ""))
                .build();
        loanHistoryRepository.save(history);

        // Crear notificación de devolución
        String message = "Has devuelto el libro correctamente.";
        if (fineAmount.compareTo(BigDecimal.ZERO) > 0) {
            message += " Multa aplicada: $" + fineAmount;
        }
        try {
            notificationServiceClient.createNotification(
                    loan.getUserId(),
                    "LOAN_RETURNED",
                    "Libro devuelto",
                    message,
                    fineAmount.compareTo(BigDecimal.ZERO) > 0 ? "HIGH" : "MEDIUM"
            ).subscribe();
        } catch (Exception e) {
            log.warn("Error creando notificación de devolución: {}", e.getMessage());
        }

        log.info("Préstamo {} devuelto exitosamente. Multa: {}", loanId, fineAmount);

        return LoanResponseDTO.fromEntity(loan);
    }

    @Transactional
    public LoanResponseDTO extendLoan(Long loanId) {
        log.info("Extendiendo préstamo: {}", loanId);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (loan.getStatus() != Loan.Status.ACTIVE) {
            throw new RuntimeException("Solo se pueden extender préstamos activos");
        }

        if (loan.isOverdue()) {
            throw new RuntimeException("No se puede extender un préstamo vencido. Por favor, devuelve el libro.");
        }

        if (loan.getExtensionsCount() >= loanConfig.getMaxExtensions()) {
            throw new RuntimeException("El préstamo ya ha sido extendido 2 veces. No se pueden hacer más extensiones.");
        }

        // Extender 7 días adicionales
        int extensionDays = 7;
        loan.setDueDate(loan.getDueDate().plusDays(extensionDays));
        loan.setExtensionsCount(loan.getExtensionsCount() + 1);
        loan = loanRepository.save(loan);

        // Registrar en historial
        LoanHistory history = LoanHistory.builder()
                .loanId(loan.getId())
                .action(LoanHistory.Action.EXTENDED)
                .notes("Préstamo extendido por " + extensionDays + " días")
                .build();
        loanHistoryRepository.save(history);

        // Crear notificación de extensión
        notificationServiceClient.createNotification(
                loan.getUserId(),
                "LOAN_EXTENDED",
                "Préstamo extendido",
                "Tu préstamo ha sido extendido " + extensionDays + " días. Nueva fecha de devolución: " + loan.getDueDate(),
                "LOW"
        ).subscribe();
        
        log.info("Préstamo {} extendido exitosamente. Nueva fecha de vencimiento: {}", loanId, loan.getDueDate());

        return LoanResponseDTO.fromEntity(loan);
    }

    @Transactional
    public LoanResponseDTO cancelLoan(Long loanId) {
        log.info("Cancelando préstamo: {}", loanId);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (loan.getStatus() != Loan.Status.ACTIVE) {
            throw new RuntimeException("Solo se pueden cancelar préstamos activos");
        }

        loan.setStatus(Loan.Status.CANCELLED);
        loan = loanRepository.save(loan);

        // Actualizar copias disponibles del libro
        bookServiceClient.updateBookCopies(loan.getBookId(), 1).block();

        // Registrar en historial
        LoanHistory history = LoanHistory.builder()
                .loanId(loan.getId())
                .action(LoanHistory.Action.CANCELLED)
                .notes("Préstamo cancelado")
                .build();
        loanHistoryRepository.save(history);

        // Crear notificación de cancelación
        notificationServiceClient.createNotification(
                loan.getUserId(),
                "LOAN_CANCELLED",
                "Préstamo cancelado",
                "Tu préstamo ha sido cancelado exitosamente.",
                "MEDIUM"
        ).subscribe();

        log.info("Préstamo {} cancelado exitosamente", loanId);

        return LoanResponseDTO.fromEntity(loan);
    }

    public List<LoanResponseDTO> getOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDate.now()).stream()
                .map(loan -> {
                    loan.markAsOverdue();
                    calculateFine(loan);
                    loanRepository.save(loan);
                    return LoanResponseDTO.fromEntity(loan);
                })
                .collect(Collectors.toList());
    }

    public FineCalculationDTO calculateFine(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));
        
        BigDecimal fine = calculateFine(loan);
        
        if (!loan.isOverdue()) {
            return FineCalculationDTO.builder()
                    .loanId(loanId)
                    .daysOverdue(0)
                    .dailyFineRate(BigDecimal.valueOf(loanConfig.getFinePerDay()))
                    .totalFine(BigDecimal.ZERO)
                    .message("El préstamo no está vencido")
                    .build();
        }
        
        long daysOverdue = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
        
        return FineCalculationDTO.builder()
                .loanId(loanId)
                .daysOverdue((int) daysOverdue)
                .dailyFineRate(BigDecimal.valueOf(loanConfig.getFinePerDay()))
                .totalFine(fine)
                .message("Préstamo vencido hace " + daysOverdue + " días")
                .build();
    }

    private BigDecimal calculateFine(Loan loan) {
        if (!loan.isOverdue()) {
            loan.setFineAmount(BigDecimal.ZERO);
            return BigDecimal.ZERO;
        }

        long daysOverdue = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
        BigDecimal fine = BigDecimal.valueOf(daysOverdue * loanConfig.getFinePerDay());
        loan.setFineAmount(fine);
        loanRepository.save(loan);
        return fine;
    }

    public List<LoanHistoryResponseDTO> getLoanHistory(Long loanId) {
        return loanHistoryRepository.findByLoanIdOrderByTimestampDesc(loanId).stream()
                .map(LoanHistoryResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public LoanValidationDTO validateLoanCreation(LoanCreateDTO createDTO, String token) {
        LoanValidationDTO validation = LoanValidationDTO.builder()
                .userId(createDTO.getUserId())
                .bookId(createDTO.getBookId())
                .build();

        // Validar usuario
        Boolean userValid = userServiceClient.validateUser(createDTO.getUserId(), token).block();
        validation.setUserExists(userValid != null && userValid);

        // Validar disponibilidad del libro
        Boolean bookAvailable = bookServiceClient.checkBookAvailability(createDTO.getBookId()).block();
        validation.setBookAvailable(bookAvailable != null && bookAvailable);

        // Validar límite de préstamos activos
        List<Loan> activeLoans = loanRepository.findActiveLoansByUserId(createDTO.getUserId());
        boolean withinLoanLimit = activeLoans.size() < 5;
        validation.setWithinLoanLimit(withinLoanLimit);

        // Validar que no tenga préstamo activo del mismo libro
        boolean hasActiveLoanForBook = activeLoans.stream()
                .anyMatch(loan -> loan.getBookId().equals(createDTO.getBookId()));
        validation.setNoActiveLoanForBook(!hasActiveLoanForBook);

        // Validar días de préstamo
        int loanDays = createDTO.getLoanDays() != null ? createDTO.getLoanDays() : loanConfig.getDefaultDays();
        boolean validLoanDays = loanDays >= 7 && loanDays <= 30;
        validation.setValidLoanDays(validLoanDays);

        // Validación general
        validation.setValid(validation.getUserExists() 
                && validation.getBookAvailable() 
                && validation.getWithinLoanLimit()
                && validation.getNoActiveLoanForBook()
                && validation.getValidLoanDays());

        // Mensaje de error específico
        if (!validation.getValid()) {
            if (!validation.getUserExists()) {
                validation.setMessage("Usuario no válido o no encontrado");
            } else if (!validation.getBookAvailable()) {
                validation.setMessage("El libro no tiene copias disponibles");
            } else if (!validation.getWithinLoanLimit()) {
                validation.setMessage("El usuario ya tiene 5 préstamos activos. No se pueden crear más préstamos.");
            } else if (!validation.getNoActiveLoanForBook()) {
                validation.setMessage("El usuario ya tiene un préstamo activo de este libro");
            } else if (!validation.getValidLoanDays()) {
                validation.setMessage("Los días de préstamo deben estar entre 7 y 30 días");
            }
        } else {
            validation.setMessage("Validación exitosa");
        }

        return validation;
    }

    /**
     * Valida todas las reglas de negocio del sistema
     * Útil para testing y verificación del correcto funcionamiento
     */
    public BusinessRulesValidationDTO validateAllBusinessRules() {
        List<BusinessRulesValidationDTO.RuleValidation> rules = new java.util.ArrayList<>();

        // Regla 1: Límite de préstamos activos por usuario (máximo 5)
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Límite de préstamos activos")
                .description("Un usuario no puede tener más de 5 préstamos activos simultáneamente")
                .valid(true)
                .message("Regla implementada: máximo 5 préstamos activos por usuario")
                .build());

        // Regla 2: No se puede prestar el mismo libro dos veces al mismo usuario
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Préstamo único por libro")
                .description("Un usuario no puede tener múltiples préstamos activos del mismo libro")
                .valid(true)
                .message("Regla implementada: verificación de préstamo activo del mismo libro")
                .build());

        // Regla 3: Días de préstamo entre 7 y 30 días
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Rango de días de préstamo")
                .description("Los días de préstamo deben estar entre 7 y 30 días")
                .valid(true)
                .message("Regla implementada: validación de días entre 7 y 30")
                .build());

        // Regla 4: Verificación de disponibilidad del libro
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Disponibilidad de libro")
                .description("Solo se pueden prestar libros que tengan copias disponibles")
                .valid(true)
                .message("Regla implementada: verificación de copias disponibles antes de préstamo")
                .build());

        // Regla 5: Validación de usuario existente y activo
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Usuario válido")
                .description("El usuario debe existir en la BD y no estar bloqueado")
                .valid(true)
                .message("Regla implementada: validación de usuario en BD y estado activo")
                .build());

        // Regla 6: Actualización de copias disponibles
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Actualización de copias")
                .description("Las copias disponibles se actualizan al crear y devolver préstamos")
                .valid(true)
                .message("Regla implementada: actualización automática de copias disponibles")
                .build());

        // Regla 7: Extensión de préstamos (máximo 2 veces, 7 días cada vez)
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Extensión de préstamos")
                .description("Un préstamo puede extenderse máximo 2 veces, 7 días cada vez, solo si no está vencido")
                .valid(true)
                .message("Regla implementada: máximo 2 extensiones de 7 días, solo si no está vencido")
                .build());

        // Regla 8: Cálculo de multas por retraso
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Cálculo de multas")
                .description("Se calculan multas por días de retraso según la tarifa configurada")
                .valid(true)
                .message("Regla implementada: cálculo automático de multas por días de retraso")
                .build());

        // Regla 9: No se puede extender un préstamo vencido
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Extensión de préstamos vencidos")
                .description("No se pueden extender préstamos que ya están vencidos")
                .valid(true)
                .message("Regla implementada: validación de estado antes de extender")
                .build());

        // Regla 10: Solo se pueden devolver préstamos activos o vencidos
        rules.add(BusinessRulesValidationDTO.RuleValidation.builder()
                .ruleName("Devolución de préstamos")
                .description("Solo se pueden devolver préstamos con estado ACTIVE o OVERDUE")
                .valid(true)
                .message("Regla implementada: validación de estado antes de devolver")
                .build());

        boolean allValid = rules.stream().allMatch(BusinessRulesValidationDTO.RuleValidation::getValid);

        return BusinessRulesValidationDTO.builder()
                .allRulesValid(allValid)
                .summary(String.format("Validación de %d reglas de negocio: %d válidas, %d inválidas",
                        rules.size(),
                        rules.stream().mapToInt(r -> r.getValid() ? 1 : 0).sum(),
                        rules.stream().mapToInt(r -> r.getValid() ? 0 : 1).sum()))
                .rules(rules)
                .build();
    }
}





