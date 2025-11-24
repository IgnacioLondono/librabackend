package com.library.loans.service;

import com.library.loans.client.BookServiceClient;
import com.library.loans.client.UserServiceClient;
import com.library.loans.config.LoanConfig;
import com.library.loans.dto.LoanCreateDTO;
import com.library.loans.dto.LoanResponseDTO;
import com.library.loans.model.Loan;
import com.library.loans.repository.LoanHistoryRepository;
import com.library.loans.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanHistoryRepository loanHistoryRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private BookServiceClient bookServiceClient;

    @Mock
    private LoanConfig loanConfig;

    @InjectMocks
    private LoanService loanService;

    private Loan testLoan;
    private LoanCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        testLoan = Loan.builder()
                .id(1L)
                .userId(1L)
                .bookId(1L)
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(Loan.Status.ACTIVE)
                .fineAmount(BigDecimal.ZERO)
                .build();

        createDTO = new LoanCreateDTO();
        createDTO.setUserId(1L);
        createDTO.setBookId(1L);
        createDTO.setLoanDays(14);
    }

    @Test
    void testGetLoanById_Success() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));

        LoanResponseDTO result = loanService.getLoanById(1L);

        assertNotNull(result);
        assertEquals(testLoan.getId(), result.getId());
    }

    @Test
    void testGetLoanById_NotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> loanService.getLoanById(1L));
    }

    @Test
    void testGetLoansByUserId_Success() {
        when(loanRepository.findByUserId(1L)).thenReturn(new ArrayList<>());

        var result = loanService.getLoansByUserId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

