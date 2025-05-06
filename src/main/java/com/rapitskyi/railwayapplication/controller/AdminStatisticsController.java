package com.rapitskyi.railwayapplication.controller;

import com.rapitskyi.railwayapplication.dto.SearchDTOs;
import com.rapitskyi.railwayapplication.service.TicketStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatisticsController {

    private final TicketStatisticsService ticketStatisticsService;

    @GetMapping("/tickets")
    public ResponseEntity<SearchDTOs.TicketStatistics> getTicketStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer routeId,
            @RequestParam(required = false) Integer departureStationId,
            @RequestParam(required = false) Integer arrivalStationId) {
        
        SearchDTOs.TicketStatistics statistics = ticketStatisticsService.generateTicketStatistics(
                startDate, endDate, routeId, departureStationId, arrivalStationId);
        
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/tickets")
    public ResponseEntity<SearchDTOs.TicketStatistics> getTicketStatisticsPost(
            @RequestBody SearchDTOs.TicketStatisticsRequest request) {
        
        SearchDTOs.TicketStatistics statistics = ticketStatisticsService.generateTicketStatistics(
                request.getStartDate(),
                request.getEndDate(),
                request.getRouteId(),
                request.getDepartureStationId(),
                request.getArrivalStationId());
        
        return ResponseEntity.ok(statistics);
    }
}