package com.rapitskyi.railwayapplication.controller;

import com.rapitskyi.railwayapplication.dto.TicketDTO;
import com.rapitskyi.railwayapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conductor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CONDUCTOR') or hasRole('ADMIN')")
public class ConductorController {

    private final TicketService ticketService;

    @GetMapping("/tickets/verify/{ticketNumber}")
    public ResponseEntity<TicketDTO> verifyTicket(@PathVariable String ticketNumber) {
        TicketDTO ticket = ticketService.getTicketByNumber(ticketNumber);
        return ResponseEntity.ok(ticket);
    }
}