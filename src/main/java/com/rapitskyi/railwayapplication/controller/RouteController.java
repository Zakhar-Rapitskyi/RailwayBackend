package com.rapitskyi.railwayapplication.controller;

import com.rapitskyi.railwayapplication.dto.RouteDTO;
import com.rapitskyi.railwayapplication.dto.RouteStationDTO;
import com.rapitskyi.railwayapplication.entity.Route;
import com.rapitskyi.railwayapplication.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable Integer id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteDTO> createRoute(@Valid @RequestBody Route route) {
        return ResponseEntity.ok(routeService.createRoute(route));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteDTO> updateRoute(@PathVariable Integer id, @Valid @RequestBody Route routeDetails) {
        return ResponseEntity.ok(routeService.updateRoute(id, routeDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoute(@PathVariable Integer id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stations")
    public ResponseEntity<List<RouteStationDTO>> getRouteStations(@PathVariable Integer id) {
        return ResponseEntity.ok(routeService.getRouteStations(id));
    }

    @PostMapping("/{routeId}/stations/{stationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteStationDTO> addStationToRoute(
            @PathVariable Integer routeId,
            @PathVariable Integer stationId,
            @RequestBody Map<String, Integer> request) {
        
        Integer stationOrder = request.get("stationOrder");
        Integer distanceFromStart = request.get("distanceFromStart");
        
        return ResponseEntity.ok(routeService.addStationToRoute(routeId, stationId, stationOrder, distanceFromStart));
    }

    @DeleteMapping("/{routeId}/stations/{stationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeStationFromRoute(
            @PathVariable Integer routeId,
            @PathVariable Integer stationId) {
        
        routeService.removeStationFromRoute(routeId, stationId);
        return ResponseEntity.noContent().build();
    }
}