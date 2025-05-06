package com.rapitskyi.railwayapplication.service;

import com.rapitskyi.railwayapplication.dto.StationDTO;
import com.rapitskyi.railwayapplication.entity.Station;
import com.rapitskyi.railwayapplication.exception.CustomExceptions.ResourceAlreadyExistsException;
import com.rapitskyi.railwayapplication.exception.CustomExceptions.ResourceNotFoundException;
import com.rapitskyi.railwayapplication.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    public List<StationDTO> getAllStations() {
        return stationRepository.findAll().stream()
                .map(StationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public StationDTO getStationById(Integer id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + id));
        return StationDTO.fromEntity(station);
    }

    public StationDTO getStationByName(String name) {
        Station station = stationRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with name: " + name));
        return StationDTO.fromEntity(station);
    }

    public StationDTO createStation(Station station) {
        Optional<Station> existingStation = stationRepository.findByName(station.getName());
        if (existingStation.isPresent()) {
            throw new ResourceAlreadyExistsException("Station already exists with name: " + station.getName());
        }
        
        Station savedStation = stationRepository.save(station);
        return StationDTO.fromEntity(savedStation);
    }

    public StationDTO updateStation(Integer id, Station stationDetails) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + id));

        Optional<Station> existingStation = stationRepository.findByName(stationDetails.getName());
        if (existingStation.isPresent() && !existingStation.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Station already exists with name: " + stationDetails.getName());
        }
        
        station.setName(stationDetails.getName());
        Station updatedStation = stationRepository.save(station);
        return StationDTO.fromEntity(updatedStation);
    }

    public void deleteStation(Integer id) {
        if (!stationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Station not found with id: " + id);
        }
        stationRepository.deleteById(id);
    }
}