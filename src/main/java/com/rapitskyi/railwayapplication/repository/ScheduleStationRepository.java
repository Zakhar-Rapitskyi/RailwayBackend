package com.rapitskyi.railwayapplication.repository;

import com.rapitskyi.railwayapplication.entity.RouteStation;
import com.rapitskyi.railwayapplication.entity.Schedule;
import com.rapitskyi.railwayapplication.entity.ScheduleStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleStationRepository extends JpaRepository<ScheduleStation, Integer> {
    List<ScheduleStation> findByScheduleOrderByRouteStation_StationOrder(Schedule schedule);
    
    Optional<ScheduleStation> findByScheduleAndRouteStation(Schedule schedule, RouteStation routeStation);
    
    @Query("SELECT ss FROM ScheduleStation ss " +
           "WHERE ss.schedule.id = :scheduleId " +
           "AND ss.routeStation.station.id = :stationId")
    Optional<ScheduleStation> findByScheduleIdAndStationId(Integer scheduleId, Integer stationId);
}