package com.rapitskyi.railwayapplication.dto;

import com.rapitskyi.railwayapplication.entity.RouteStation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteStationDTO {
    private Integer id;
    private Integer routeId;
    private StationDTO station;
    private Integer stationOrder;
    private Integer distanceFromStart;

    public static RouteStationDTO fromEntity(RouteStation routeStation) {
        if (routeStation == null) return null;
        
        RouteStationDTO dto = new RouteStationDTO();
        dto.setId(routeStation.getId());
        dto.setRouteId(routeStation.getRoute().getId());
        dto.setStation(StationDTO.fromEntity(routeStation.getStation()));
        dto.setStationOrder(routeStation.getStationOrder());
        dto.setDistanceFromStart(routeStation.getDistanceFromStart());
        return dto;
    }
}