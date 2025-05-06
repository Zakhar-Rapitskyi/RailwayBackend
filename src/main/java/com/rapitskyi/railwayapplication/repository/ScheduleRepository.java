package com.rapitskyi.railwayapplication.repository;

import com.rapitskyi.railwayapplication.entity.Route;
import com.rapitskyi.railwayapplication.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByDepartureDate(LocalDate departureDate);
    
    List<Schedule> findByDepartureDateAndRoute(LocalDate departureDate, Route route);

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.route r " +
            "JOIN RouteStation depRs ON depRs.route.id = r.id AND depRs.station.id = :departureStationId " +
            "JOIN RouteStation arrRs ON arrRs.route.id = r.id AND arrRs.station.id = :arrivalStationId " +
            "WHERE s.departureDate = :departureDate " +
            "AND depRs.stationOrder < arrRs.stationOrder ")
    List<Schedule> findSchedulesByDepartureDateAndStations(LocalDate departureDate, Integer departureStationId, Integer arrivalStationId);
    @Query("SELECT s FROM Schedule s WHERE s.departureDate BETWEEN :startDate AND :endDate")
    List<Schedule> findSchedulesBetweenDates(LocalDate startDate, LocalDate endDate);
}