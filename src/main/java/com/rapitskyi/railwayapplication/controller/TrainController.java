package com.rapitskyi.railwayapplication.controller;

import com.rapitskyi.railwayapplication.dto.TrainDTO;
import com.rapitskyi.railwayapplication.entity.Train;
import com.rapitskyi.railwayapplication.service.TrainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    @GetMapping
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        return ResponseEntity.ok(trainService.getAllTrains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainDTO> getTrainById(@PathVariable Integer id) {
        return ResponseEntity.ok(trainService.getTrainById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainDTO> createTrain(@Valid @RequestBody Train train) {
        return ResponseEntity.ok(trainService.createTrain(train));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainDTO> updateTrain(@PathVariable Integer id, @Valid @RequestBody Train trainDetails) {
        return ResponseEntity.ok(trainService.updateTrain(id, trainDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTrain(@PathVariable Integer id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }
}