package com.rapitskyi.railwayapplication.controller;

import com.rapitskyi.railwayapplication.dto.StationDTO;
import com.rapitskyi.railwayapplication.entity.Station;
import com.rapitskyi.railwayapplication.service.StationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping
    public ResponseEntity<List<StationDTO>> getAllStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationDTO> getStationById(@PathVariable Integer id) {
        return ResponseEntity.ok(stationService.getStationById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<StationDTO> getStationByName(@RequestParam String name) {
        return ResponseEntity.ok(stationService.getStationByName(name));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StationDTO> createStation(@Valid @RequestBody Station station) {
        return ResponseEntity.ok(stationService.createStation(station));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StationDTO> updateStation(@PathVariable Integer id, @Valid @RequestBody Station stationDetails) {
        return ResponseEntity.ok(stationService.updateStation(id, stationDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStation(@PathVariable Integer id) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }
}