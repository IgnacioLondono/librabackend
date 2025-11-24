package com.library.reports.service;

import com.library.reports.config.MicroservicesConfig;
import com.library.reports.dto.DashboardStatisticsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportsServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private MicroservicesConfig microservicesConfig;

    @InjectMocks
    private ReportsService reportsService;

    @BeforeEach
    void setUp() {
        // Configurar mocks básicos
        MicroservicesConfig.BookCatalog bookCatalog = new MicroservicesConfig.BookCatalog();
        bookCatalog.setUrl("http://localhost:8082");
        
        MicroservicesConfig.UserManagement userManagement = new MicroservicesConfig.UserManagement();
        userManagement.setUrl("http://localhost:8081");
        
        MicroservicesConfig.LoanManagement loanManagement = new MicroservicesConfig.LoanManagement();
        loanManagement.setUrl("http://localhost:8083");

        when(microservicesConfig.getBookCatalog()).thenReturn(bookCatalog);
        when(microservicesConfig.getUserManagement()).thenReturn(userManagement);
        when(microservicesConfig.getLoanManagement()).thenReturn(loanManagement);
    }

    @Test
    void testGetDashboardStatistics_ReturnsDTO() {
        // Este test verifica que el método retorna un DTO válido
        // En un entorno real, necesitarías mockear las llamadas WebClient
        DashboardStatisticsDTO result = reportsService.getDashboardStatistics();

        assertNotNull(result);
        assertNotNull(result.getTotalBooks());
        assertNotNull(result.getTotalUsers());
        assertNotNull(result.getTotalLoans());
    }
}

