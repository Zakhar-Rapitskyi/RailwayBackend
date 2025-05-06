package com.rapitskyi.railwayapplication.dto;

import com.rapitskyi.railwayapplication.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private Integer id;
    private TrainDTO train;
    private RouteDTO route;
    private LocalDate departureDate;
    private List<ScheduleStationDTO> scheduleStations = new ArrayList<>();

    public static ScheduleDTO fromEntity(Schedule schedule) {
        if (schedule == null) return null;
        
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setTrain(TrainDTO.fromEntity(schedule.getTrain()));
        dto.setRoute(RouteDTO.fromEntity(schedule.getRoute()));
        dto.setDepartureDate(schedule.getDepartureDate());
        
        if (schedule.getScheduleStations() != null && !schedule.getScheduleStations().isEmpty()) {
            dto.setScheduleStations(schedule.getScheduleStations().stream()
                    .map(ScheduleStationDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    public Schedule toEntity() {
        Schedule schedule = new Schedule();
        schedule.setId(this.id);
        schedule.setDepartureDate(this.departureDate);
        return schedule;
    }
}