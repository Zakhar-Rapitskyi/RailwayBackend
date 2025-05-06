package com.rapitskyi.railwayapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "station")
    private List<RouteStation> routeStations = new ArrayList<>();

    @OneToMany(mappedBy = "departureStation")
    private List<Ticket> departureTickets = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalStation")
    private List<Ticket> arrivalTickets = new ArrayList<>();
}