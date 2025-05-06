package com.rapitskyi.railwayapplication.controller;

import com.rapitskyi.railwayapplication.dto.ScheduleDTO;
import com.rapitskyi.railwayapplication.dto.ScheduleStationDTO;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.RouteSearchRequest;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.RouteSearchResult;
import com.rapitskyi.railwayapplication.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Integer id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScheduleDTO> createSchedule(
            @RequestParam Integer trainId,
            @RequestParam Integer routeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate) {
        
        return ResponseEntity.ok(scheduleService.createSchedule(trainId, routeId, departureDate));
    }

    @PostMapping("/{scheduleId}/stations/{routeStationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScheduleStationDTO> setScheduleStationTime(
            @PathVariable Integer scheduleId,
            @PathVariable Integer routeStationId,
            @RequestBody Map<String, String> request) {
        
        LocalTime arrivalTime = LocalTime.parse(request.get("arrivalTime"));
        
        return ResponseEntity.ok(scheduleService.setScheduleStationTime(scheduleId, routeStationId, arrivalTime));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Integer id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<RouteSearchResult>> searchRoutes(@Valid @RequestBody RouteSearchRequest request) {
        return ResponseEntity.ok(scheduleService.searchRoutes(request));
    }
}