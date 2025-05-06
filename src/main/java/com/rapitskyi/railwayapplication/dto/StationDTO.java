package com.rapitskyi.railwayapplication.dto;

import com.rapitskyi.railwayapplication.entity.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO {
    private Integer id;
    private String name;

    public static StationDTO fromEntity(Station station) {
        if (station == null) return null;
        
        StationDTO dto = new StationDTO();
        dto.setId(station.getId());
        dto.setName(station.getName());
        return dto;
    }

    public Station toEntity() {
        Station station = new Station();
        station.setId(this.id);
        station.setName(this.name);
        return station;
    }
}