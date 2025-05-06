package com.rapitskyi.railwayapplication.dto;

import com.rapitskyi.railwayapplication.entity.ScheduleStation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleStationDTO {
    private Integer id;
    private Integer scheduleId;
    private RouteStationDTO routeStation;
    private LocalTime arrivalTime;

    public static ScheduleStationDTO fromEntity(ScheduleStation scheduleStation) {
        if (scheduleStation == null) return null;
        
        ScheduleStationDTO dto = new ScheduleStationDTO();
        dto.setId(scheduleStation.getId());
        dto.setScheduleId(scheduleStation.getSchedule().getId());
        dto.setRouteStation(RouteStationDTO.fromEntity(scheduleStation.getRouteStation()));
        dto.setArrivalTime(scheduleStation.getArrivalTime());
        return dto;
    }
}