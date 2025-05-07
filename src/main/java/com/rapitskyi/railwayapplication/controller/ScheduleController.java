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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final JdbcTemplate jdbcTemplate;

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

    @PutMapping("/{id}/train")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateScheduleTrain(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> request) {

        Integer trainId = request.get("trainId");

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("update_schedule_train")
                .declareParameters(
                        new SqlParameter("p_schedule_id", Types.INTEGER),
                        new SqlParameter("p_train_id", Types.INTEGER),
                        new SqlOutParameter("p_success", Types.BOOLEAN),
                        new SqlOutParameter("p_message", Types.VARCHAR)
                );

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_schedule_id", id)
                .addValue("p_train_id", trainId);

        Map<String, Object> out = jdbcCall.execute(in);

        Boolean success = (Boolean) out.get("p_success");
        String message = (String) out.get("p_message");

        if (success) {
            ScheduleDTO updatedSchedule = scheduleService.getScheduleById(id);
            return ResponseEntity.ok(updatedSchedule);
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", message));
        }
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