package com.rapitskyi.railwayapplication.dto;

import com.rapitskyi.railwayapplication.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Integer id;
    private String ticketNumber;
    private Integer userId;
    private Integer scheduleId;
    private Integer carNumber;
    private Integer seatNumber;
    private StationDTO departureStation;
    private StationDTO arrivalStation;
    private LocalDateTime departureDatetime;
    private LocalDateTime arrivalDatetime;
    private String qrCode;
    private UserDTO user;

    public static TicketDTO fromEntity(Ticket ticket) {
        if (ticket == null) return null;
        
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setUserId(ticket.getUser().getId());
        dto.setScheduleId(ticket.getSchedule().getId());
        dto.setCarNumber(ticket.getCarNumber());
        dto.setSeatNumber(ticket.getSeatNumber());
        dto.setDepartureStation(StationDTO.fromEntity(ticket.getDepartureStation()));
        dto.setArrivalStation(StationDTO.fromEntity(ticket.getArrivalStation()));
        dto.setDepartureDatetime(ticket.getDepartureDatetime());
        dto.setArrivalDatetime(ticket.getArrivalDatetime());
        dto.setQrCode(ticket.getQrCode());
        dto.setUser(UserDTO.fromEntity(ticket.getUser()));
        return dto;
    }
}