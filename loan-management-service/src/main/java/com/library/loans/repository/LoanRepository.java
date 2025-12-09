package com.library.loans.repository;

import com.library.loans.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUserId(Long userId);

    List<Loan> findByUserIdAndStatus(Long userId, Loan.Status status);

    List<Loan> findByBookId(Long bookId);

    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.dueDate < :today")
    List<Loan> findOverdueLoans(@Param("today") LocalDate today);

    @Query("SELECT l FROM Loan l WHERE l.userId = :userId AND l.status = 'ACTIVE'")
    List<Loan> findActiveLoansByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM Loan l WHERE l.dueDate = :dueDate AND l.status = :status")
    List<Loan> findByDueDateAndStatus(@Param("dueDate") LocalDate dueDate, @Param("status") Loan.Status status);

    Optional<Loan> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT l FROM Loan l WHERE l.bookId = :bookId AND (l.status = 'ACTIVE' OR l.status = 'OVERDUE')")
    List<Loan> findActiveLoansByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.userId = :userId AND (l.status = 'ACTIVE' OR l.status = 'OVERDUE')")
    boolean hasActiveLoansByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.bookId = :bookId AND (l.status = 'ACTIVE' OR l.status = 'OVERDUE')")
    boolean hasActiveLoansByBookId(@Param("bookId") Long bookId);
}





