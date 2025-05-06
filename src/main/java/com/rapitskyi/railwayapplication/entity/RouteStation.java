package com.rapitskyi.railwayapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "route_stations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "station_order", nullable = false)
    private Integer stationOrder;

    @Column(name = "distance_from_start", nullable = false)
    private Integer distanceFromStart;

    @OneToMany(mappedBy = "routeStation")
    private List<ScheduleStation> scheduleStations = new ArrayList<>();
}