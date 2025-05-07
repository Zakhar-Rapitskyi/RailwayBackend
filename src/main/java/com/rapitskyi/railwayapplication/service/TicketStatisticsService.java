package com.rapitskyi.railwayapplication.service;

import com.rapitskyi.railwayapplication.dto.TicketStatisticsDTO;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.TicketStatisticsRequest;
import com.rapitskyi.railwayapplication.entity.TicketStatistics;
import com.rapitskyi.railwayapplication.repository.TicketStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketStatisticsService {

    private final TicketStatisticsRepository ticketStatisticsRepository;
    private final JdbcTemplate jdbcTemplate;
    /**
     * Отримати статистику квитків на основі запиту
     *
     * @param request критерії пошуку
     * @return статистика квитків
     */
    public List<TicketStatisticsDTO> getTicketStatistics(TicketStatisticsRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        Integer routeId = request.getRouteId();

        List<TicketStatistics> ticketStatistics;
        if (routeId != null) {
            ticketStatistics = ticketStatisticsRepository.findByRouteIdAndDateBetweenOrderByDateAsc(routeId, startDate, endDate);
        } else {
            ticketStatistics = ticketStatisticsRepository.findByDateBetweenOrderByDateAsc(startDate, endDate);
        }

        return ticketStatistics.stream()
                .map(TicketStatisticsDTO::fromEntity)
                .collect(Collectors.toList());
    }


    public void updateStatisticsForDate(LocalDate date) {
        jdbcTemplate.update("CALL update_ticket_statistics(?)", date);
    }
}