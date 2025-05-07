package com.rapitskyi.railwayapplication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class SearchDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteSearchRequest {
        private Integer departureStationId;
        private Integer arrivalStationId;
        private LocalDate departureDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteSearchResult {
        private Integer scheduleId;
        private String routeName;
        private String trainName;
        private Integer totalCars;
        private LocalDate departureDate;
        private StationDTO departureStation;
        private StationDTO arrivalStation;
        private LocalTime departureTime;
        private LocalTime arrivalTime;
        private Integer durationMinutes;
        private Integer distanceKm;
        private Integer availableSeats;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketBookingRequest {
        private Integer scheduleId;
        private Integer departureStationId;
        private Integer arrivalStationId;
        private Integer carNumber;
        private Integer seatNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarSeatInfo {
        private Integer carNumber;
        private List<Integer> occupiedSeats;
        private Integer totalSeats = 50;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketStatisticsRequest {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        private Integer routeId;
    }
}