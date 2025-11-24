package com.library.reports.controller;

import com.library.reports.dto.DashboardStatisticsDTO;
import com.library.reports.service.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "API para generación de reportes y estadísticas")
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping("/dashboard")
    @Operation(summary = "Estadísticas del dashboard", description = "Obtiene las estadísticas generales del dashboard")
    public ResponseEntity<DashboardStatisticsDTO> getDashboardStatistics() {
        DashboardStatisticsDTO statistics = reportsService.getDashboardStatistics();
        return ResponseEntity.ok(statistics);
    }
}






