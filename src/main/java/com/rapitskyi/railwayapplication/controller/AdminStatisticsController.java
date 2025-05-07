package com.rapitskyi.railwayapplication.controller;

import com.rapitskyi.railwayapplication.dto.SearchDTOs;
import com.rapitskyi.railwayapplication.dto.TicketStatisticsDTO;
import com.rapitskyi.railwayapplication.service.TicketStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatisticsController {

    private final TicketStatisticsService ticketStatisticsService;

    @PostMapping("/tickets")
    public ResponseEntity<List<TicketStatisticsDTO>> getTicketStatisticsPost(
            @RequestBody SearchDTOs.TicketStatisticsRequest request) {
        
        List<TicketStatisticsDTO> statistics = ticketStatisticsService.getTicketStatistics(request);
        
        return ResponseEntity.ok(statistics);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateTicketStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        ticketStatisticsService.updateStatisticsForDate(date);

        return ResponseEntity.ok("Statistics updated successfully for " + date);
    }
}