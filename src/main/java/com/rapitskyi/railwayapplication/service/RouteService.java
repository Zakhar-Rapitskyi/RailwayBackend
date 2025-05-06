package com.rapitskyi.railwayapplication.service;

import com.rapitskyi.railwayapplication.dto.RouteDTO;
import com.rapitskyi.railwayapplication.dto.RouteStationDTO;
import com.rapitskyi.railwayapplication.entity.Route;
import com.rapitskyi.railwayapplication.entity.RouteStation;
import com.rapitskyi.railwayapplication.entity.Station;
import com.rapitskyi.railwayapplication.exception.CustomExceptions.BadRequestException;
import com.rapitskyi.railwayapplication.exception.CustomExceptions.ResourceNotFoundException;
import com.rapitskyi.railwayapplication.repository.RouteRepository;
import com.rapitskyi.railwayapplication.repository.RouteStationRepository;
import com.rapitskyi.railwayapplication.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteStationRepository routeStationRepository;
    private final StationRepository stationRepository;

    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(RouteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public RouteDTO getRouteById(Integer id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));
        return RouteDTO.fromEntity(route);
    }

    @Transactional
    public RouteDTO createRoute(Route route) {
        Route savedRoute = routeRepository.save(route);
        return RouteDTO.fromEntity(savedRoute);
    }

    @Transactional
    public RouteDTO updateRoute(Integer id, Route routeDetails) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));
        
        route.setName(routeDetails.getName());
        Route updatedRoute = routeRepository.save(route);
        
        return RouteDTO.fromEntity(updatedRoute);
    }

    @Transactional
    public void deleteRoute(Integer id) {
        if (!routeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Route not found with id: " + id);
        }
        routeRepository.deleteById(id);
    }
    
    @Transactional
    public RouteStationDTO addStationToRoute(Integer routeId, Integer stationId, Integer stationOrder, Integer distanceFromStart) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));
                
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + stationId));

        routeStationRepository.findByRouteOrderByStationOrder(route).stream()
                .filter(rs -> rs.getStationOrder().equals(stationOrder))
                .findAny()
                .ifPresent(rs -> {
                    throw new BadRequestException("Station order " + stationOrder + " is already taken for this route");
                });

        routeStationRepository.findByRouteAndStation(route, station)
                .ifPresent(rs -> {
                    throw new BadRequestException("Station is already part of this route");
                });
        
        RouteStation routeStation = new RouteStation();
        routeStation.setRoute(route);
        routeStation.setStation(station);
        routeStation.setStationOrder(stationOrder);
        routeStation.setDistanceFromStart(distanceFromStart);
        
        RouteStation savedRouteStation = routeStationRepository.save(routeStation);
        return RouteStationDTO.fromEntity(savedRouteStation);
    }
    
    @Transactional
    public void removeStationFromRoute(Integer routeId, Integer stationId) {
        RouteStation routeStation = routeStationRepository.findByRouteIdAndStationId(routeId, stationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Station with id " + stationId + " not found in route with id " + routeId));
        
        routeStationRepository.delete(routeStation);
    }
    
    public List<RouteStationDTO> getRouteStations(Integer routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));
        
        return routeStationRepository.findByRouteOrderByStationOrder(route).stream()
                .map(RouteStationDTO::fromEntity)
                .collect(Collectors.toList());
    }
}