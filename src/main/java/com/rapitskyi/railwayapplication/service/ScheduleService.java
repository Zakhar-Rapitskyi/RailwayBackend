package com.rapitskyi.railwayapplication.service;

import com.rapitskyi.railwayapplication.dto.ScheduleDTO;
import com.rapitskyi.railwayapplication.dto.ScheduleStationDTO;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.RouteSearchRequest;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.RouteSearchResult;
import com.rapitskyi.railwayapplication.dto.StationDTO;
import com.rapitskyi.railwayapplication.entity.*;
import com.rapitskyi.railwayapplication.exception.CustomExceptions.BadRequestException;
import com.rapitskyi.railwayapplication.exception.CustomExceptions.ResourceNotFoundException;
import com.rapitskyi.railwayapplication.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final RouteStationRepository routeStationRepository;
    private final ScheduleStationRepository scheduleStationRepository;
    private final TicketRepository ticketRepository;

    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ScheduleDTO getScheduleById(Integer id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        return ScheduleDTO.fromEntity(schedule);
    }

    @Transactional
    public ScheduleDTO createSchedule(Integer trainId, Integer routeId, LocalDate departureDate) {
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + trainId));

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));

        List<RouteStation> routeStations = routeStationRepository.findByRouteOrderByStationOrder(route);
        if (routeStations.isEmpty()) {
            throw new BadRequestException("Route must have at least one station");
        }

        Schedule schedule = new Schedule();
        schedule.setTrain(train);
        schedule.setRoute(route);
        schedule.setDepartureDate(departureDate);

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return ScheduleDTO.fromEntity(savedSchedule);
    }

    @Transactional
    public ScheduleStationDTO setScheduleStationTime(Integer scheduleId, Integer routeStationId, LocalTime arrivalTime) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));

        RouteStation routeStation = routeStationRepository.findById(routeStationId)
                .orElseThrow(() -> new ResourceNotFoundException("Route station not found with id: " + routeStationId));

        if (!routeStation.getRoute().getId().equals(schedule.getRoute().getId())) {
            throw new BadRequestException("Route station does not belong to the schedule's route");
        }

        ScheduleStation scheduleStation = scheduleStationRepository
                .findByScheduleAndRouteStation(schedule, routeStation)
                .orElse(new ScheduleStation());

        scheduleStation.setSchedule(schedule);
        scheduleStation.setRouteStation(routeStation);
        scheduleStation.setArrivalTime(arrivalTime);

        ScheduleStation savedScheduleStation = scheduleStationRepository.save(scheduleStation);
        return ScheduleStationDTO.fromEntity(savedScheduleStation);
    }

    @Transactional
    public void deleteSchedule(Integer id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    public List<RouteSearchResult> searchRoutes(RouteSearchRequest request) {
        if (request.getDepartureStationId() == null || request.getArrivalStationId() == null || request.getDepartureDate() == null) {
            throw new BadRequestException("Departure station, arrival station, and departure date are required");
        }

        stationRepository.findById(request.getDepartureStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Departure station not found with id: " + request.getDepartureStationId()));

        stationRepository.findById(request.getArrivalStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Arrival station not found with id: " + request.getArrivalStationId()));

        List<Schedule> schedules = scheduleRepository.findSchedulesByDepartureDateAndStations(
                request.getDepartureDate(),
                request.getDepartureStationId(),
                request.getArrivalStationId()
        );

        List<RouteSearchResult> results = new ArrayList<>();

        for (Schedule schedule : schedules) {
            RouteStation departureRouteStation = routeStationRepository
                    .findByRouteIdAndStationId(schedule.getRoute().getId(), request.getDepartureStationId())
                    .orElseThrow();

            RouteStation arrivalRouteStation = routeStationRepository
                    .findByRouteIdAndStationId(schedule.getRoute().getId(), request.getArrivalStationId())
                    .orElseThrow();

            if (departureRouteStation.getStationOrder() >= arrivalRouteStation.getStationOrder()) {
                continue;
            }

            ScheduleStation departureScheduleStation = scheduleStationRepository
                    .findByScheduleAndRouteStation(schedule, departureRouteStation)
                    .orElse(null);

            ScheduleStation arrivalScheduleStation = scheduleStationRepository
                    .findByScheduleAndRouteStation(schedule, arrivalRouteStation)
                    .orElse(null);

            if (departureScheduleStation == null || arrivalScheduleStation == null ||
                    departureScheduleStation.getArrivalTime() == null || arrivalScheduleStation.getArrivalTime() == null) {
                continue;
            }

            long durationMinutes = Duration.between(
                    departureScheduleStation.getArrivalTime(),
                    arrivalScheduleStation.getArrivalTime()
            ).toMinutes();

            int distanceKm = arrivalRouteStation.getDistanceFromStart() - departureRouteStation.getDistanceFromStart();

            long occupiedSeats = ticketRepository.countBookedSeatsByScheduleIdAndCarNumber(schedule.getId(), 1);
            int availableSeats = schedule.getTrain().getTotalCars() * 50 - (int) occupiedSeats;

            RouteSearchResult result = new RouteSearchResult(
                    schedule.getId(),
                    schedule.getRoute().getName(),
                    schedule.getTrain().getName(),
                    schedule.getTrain().getTotalCars(),
                    schedule.getDepartureDate(),
                    StationDTO.fromEntity(departureRouteStation.getStation()),
                    StationDTO.fromEntity(arrivalRouteStation.getStation()),
                    departureScheduleStation.getArrivalTime(),
                    arrivalScheduleStation.getArrivalTime(),
                    (int) durationMinutes,
                    distanceKm,
                    availableSeats
            );

            results.add(result);
        }

        return results;
    }
}