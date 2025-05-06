package com.rapitskyi.railwayapplication.controller;

import com.rapitskyi.railwayapplication.dto.SearchDTOs.CarSeatInfo;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.TicketBookingRequest;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.TicketStatistics;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.TicketStatisticsRequest;
import com.rapitskyi.railwayapplication.dto.TicketDTO;
import com.rapitskyi.railwayapplication.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Integer id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/number/{ticketNumber}")
    public ResponseEntity<TicketDTO> getTicketByNumber(@PathVariable String ticketNumber) {
        return ResponseEntity.ok(ticketService.getTicketByNumber(ticketNumber));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getCurrentUser().getId() == #userId")
    public ResponseEntity<List<TicketDTO>> getTicketsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<TicketDTO>> getCurrentUserTickets() {
        return ResponseEntity.ok(ticketService.getCurrentUserTickets());
    }

    @PostMapping
    public ResponseEntity<TicketDTO> bookTicket(@Valid @RequestBody TicketBookingRequest request) {
        return ResponseEntity.ok(ticketService.bookTicket(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable Integer id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/seats")
    public ResponseEntity<CarSeatInfo> getAvailableSeats(
            @RequestParam Integer scheduleId,
            @RequestParam Integer carNumber) {
        
        return ResponseEntity.ok(ticketService.getAvailableSeats(scheduleId, carNumber));
    }

    @PostMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketStatistics> getTicketStatistics(@Valid @RequestBody TicketStatisticsRequest request) {
        return ResponseEntity.ok(ticketService.getTicketStatistics(request));
    }
}