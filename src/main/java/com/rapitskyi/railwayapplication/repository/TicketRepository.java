package com.rapitskyi.railwayapplication.repository;

import com.rapitskyi.railwayapplication.entity.Schedule;
import com.rapitskyi.railwayapplication.entity.Ticket;
import com.rapitskyi.railwayapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUser(User user);
    
    Optional<Ticket> findByTicketNumber(String ticketNumber);
    
    List<Ticket> findBySchedule(Schedule schedule);
    
    @Query("SELECT t FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.carNumber = :carNumber AND t.seatNumber = :seatNumber")
    Optional<Ticket> findByScheduleIdAndCarNumberAndSeatNumber(Integer scheduleId, Integer carNumber, Integer seatNumber);
    
    @Query("SELECT t FROM Ticket t WHERE t.departureDatetime BETWEEN :startDate AND :endDate")
    List<Ticket> findTicketsByDepartureDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT t FROM Ticket t WHERE t.departureStation.id = :departureStationId AND t.arrivalStation.id = :arrivalStationId")
    List<Ticket> findTicketsByDepartureAndArrivalStations(Integer departureStationId, Integer arrivalStationId);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.carNumber = :carNumber")
    Long countBookedSeatsByScheduleIdAndCarNumber(Integer scheduleId, Integer carNumber);
    
    @Query("SELECT t.seatNumber FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.carNumber = :carNumber")
    List<Integer> findBookedSeatsByScheduleIdAndCarNumber(Integer scheduleId, Integer carNumber);
}