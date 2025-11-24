package com.library.loans.service;

import com.library.loans.client.NotificationServiceClient;
import com.library.loans.model.Loan;
import com.library.loans.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Scheduler para crear notificaciones automáticas de préstamos
 * - Préstamos próximos a vencer (2 días antes)
 * - Préstamos vencidos
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LoanNotificationScheduler {

    private final LoanRepository loanRepository;
    private final NotificationServiceClient notificationServiceClient;

    /**
     * Verificar préstamos próximos a vencer (2 días antes)
     * Se ejecuta diariamente a las 9:00 AM
     */
    @Scheduled(cron = "0 0 9 * * ?") // Diario a las 9:00 AM
    @Transactional
    public void checkLoansDueSoon() {
        log.info("Verificando préstamos próximos a vencer...");
        
        LocalDate twoDaysFromNow = LocalDate.now().plusDays(2);
        List<Loan> loansDueSoon = loanRepository.findByDueDateAndStatus(twoDaysFromNow, Loan.Status.ACTIVE);
        
        for (Loan loan : loansDueSoon) {
            notificationServiceClient.createNotification(
                    loan.getUserId(),
                    "LOAN_DUE",
                    "Préstamo próximo a vencer",
                    "Tu préstamo vence en 2 días. Fecha de devolución: " + loan.getDueDate(),
                    "HIGH"
            ).subscribe();
            
            log.info("Notificación de vencimiento próximo enviada para préstamo {}", loan.getId());
        }
        
        log.info("Procesados {} préstamos próximos a vencer", loansDueSoon.size());
    }

    /**
     * Verificar préstamos vencidos
     * Se ejecuta diariamente a las 10:00 AM
     */
    @Scheduled(cron = "0 0 10 * * ?") // Diario a las 10:00 AM
    @Transactional
    public void checkOverdueLoans() {
        log.info("Verificando préstamos vencidos...");
        
        List<Loan> overdueLoans = loanRepository.findOverdueLoans(LocalDate.now());
        
        for (Loan loan : overdueLoans) {
            // Marcar como vencido si aún no lo está
            if (loan.getStatus() != Loan.Status.OVERDUE) {
                loan.markAsOverdue();
                loanRepository.save(loan);
            }
            
            // Crear notificación
            long daysOverdue = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
            notificationServiceClient.createNotification(
                    loan.getUserId(),
                    "LOAN_OVERDUE",
                    "Préstamo vencido",
                    "Tu préstamo está vencido hace " + daysOverdue + " días. Por favor, devuelve el libro lo antes posible.",
                    "HIGH"
            ).subscribe();
            
            log.info("Notificación de préstamo vencido enviada para préstamo {}", loan.getId());
        }
        
        log.info("Procesados {} préstamos vencidos", overdueLoans.size());
    }
}

