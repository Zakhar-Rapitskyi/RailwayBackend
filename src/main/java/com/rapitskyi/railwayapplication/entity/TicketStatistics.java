package com.rapitskyi.railwayapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "route_name", nullable = false)
    private String routeName;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "ticket_count", nullable = false)
    private Integer ticketCount;

    @Column(name = "seat_capacity", nullable = false)
    private Integer seatCapacity;

    @Column(name = "occupancy_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal occupancyRate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}