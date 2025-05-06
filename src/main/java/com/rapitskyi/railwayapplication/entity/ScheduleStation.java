package com.rapitskyi.railwayapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "schedule_stations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "route_station_id", nullable = false)
    private RouteStation routeStation;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;
}