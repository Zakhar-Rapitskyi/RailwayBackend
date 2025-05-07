package com.rapitskyi.railwayapplication.dto;

import com.rapitskyi.railwayapplication.entity.TicketStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatisticsDTO {
    private Integer id;
    private Integer routeId;
    private String routeName;
    private LocalDate date;
    private Integer ticketCount;
    private Integer seatCapacity;
    private BigDecimal occupancyRate;
    private LocalDateTime updatedAt;

    public static TicketStatisticsDTO fromEntity(TicketStatistics statistics) {
        if (statistics == null) return null;

        TicketStatisticsDTO dto = new TicketStatisticsDTO();
        dto.setId(statistics.getId());
        dto.setRouteId(statistics.getRoute().getId());
        dto.setRouteName(statistics.getRouteName());
        dto.setDate(statistics.getDate());
        dto.setTicketCount(statistics.getTicketCount());
        dto.setSeatCapacity(statistics.getSeatCapacity());
        dto.setOccupancyRate(statistics.getOccupancyRate());
        dto.setUpdatedAt(statistics.getUpdatedAt());

        return dto;
    }
}