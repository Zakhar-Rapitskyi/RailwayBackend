package com.rapitskyi.railwayapplication.dto;

import com.rapitskyi.railwayapplication.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private Integer id;
    private String name;
    private List<RouteStationDTO> stations = new ArrayList<>();

    public static RouteDTO fromEntity(Route route) {
        if (route == null) return null;
        
        RouteDTO dto = new RouteDTO();
        dto.setId(route.getId());
        dto.setName(route.getName());
        
        if (route.getRouteStations() != null && !route.getRouteStations().isEmpty()) {
            dto.setStations(route.getRouteStations().stream()
                    .map(RouteStationDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    public Route toEntity() {
        Route route = new Route();
        route.setId(this.id);
        route.setName(this.name);
        return route;
    }
}