package com.rapitskyi.railwayapplication.repository;

import com.rapitskyi.railwayapplication.entity.TicketStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketStatisticsRepository extends JpaRepository<TicketStatistics, Integer> {
    
    List<TicketStatistics> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate);
    
    List<TicketStatistics> findByRouteIdAndDateBetweenOrderByDateAsc(Integer routeId, LocalDate startDate, LocalDate endDate);

}