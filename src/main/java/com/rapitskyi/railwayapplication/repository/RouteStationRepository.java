package com.rapitskyi.railwayapplication.repository;

import com.rapitskyi.railwayapplication.entity.Route;
import com.rapitskyi.railwayapplication.entity.RouteStation;
import com.rapitskyi.railwayapplication.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteStationRepository extends JpaRepository<RouteStation, Integer> {
    List<RouteStation> findByRouteOrderByStationOrder(Route route);
    
    Optional<RouteStation> findByRouteAndStation(Route route, Station station);
    
    @Query("SELECT rs FROM RouteStation rs WHERE rs.route.id = :routeId AND rs.station.id = :stationId")
    Optional<RouteStation> findByRouteIdAndStationId(Integer routeId, Integer stationId);
    
    @Query("SELECT rs FROM RouteStation rs WHERE rs.route.id IN " +
           "(SELECT r.id FROM Route r WHERE r.id IN " +
           "(SELECT DISTINCT rs1.route.id FROM RouteStation rs1 WHERE rs1.station.id = :departureStationId) " +
           "AND r.id IN (SELECT DISTINCT rs2.route.id FROM RouteStation rs2 WHERE rs2.station.id = :arrivalStationId))")
    List<RouteStation> findRoutesByDepartureAndArrivalStations(Integer departureStationId, Integer arrivalStationId);
}