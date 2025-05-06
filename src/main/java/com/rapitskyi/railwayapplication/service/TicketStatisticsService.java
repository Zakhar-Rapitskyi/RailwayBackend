package com.rapitskyi.railwayapplication.service;

import com.rapitskyi.railwayapplication.dto.SearchDTOs.RouteStatistics;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.TicketStatistics;
import com.rapitskyi.railwayapplication.entity.Route;
import com.rapitskyi.railwayapplication.entity.Station;
import com.rapitskyi.railwayapplication.entity.Ticket;
import com.rapitskyi.railwayapplication.repository.RouteRepository;
import com.rapitskyi.railwayapplication.repository.StationRepository;
import com.rapitskyi.railwayapplication.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketStatisticsService {

    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;

    public TicketStatistics generateTicketStatistics(
            LocalDate startDate,
            LocalDate endDate,
            Integer routeId,
            Integer departureStationId,
            Integer arrivalStationId) {

        // Конвертация дат в DateTime для запроса
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        // Получение билетов в указанном диапазоне дат
        List<Ticket> tickets = ticketRepository.findTicketsByDepartureDateRange(startDateTime, endDateTime);

        // Фильтрация по маршруту при необходимости
        if (routeId != null) {
            Route route = routeRepository.findById(routeId).orElse(null);
            if (route != null) {
                tickets = tickets.stream()
                        .filter(ticket -> ticket.getSchedule().getRoute().getId().equals(routeId))
                        .collect(Collectors.toList());
            }
        }

        // Фильтрация по станции отправления при необходимости
        if (departureStationId != null) {
            Station departureStation = stationRepository.findById(departureStationId).orElse(null);
            if (departureStation != null) {
                tickets = tickets.stream()
                        .filter(ticket -> ticket.getDepartureStation().getId().equals(departureStationId))
                        .collect(Collectors.toList());
            }
        }

        // Фильтрация по станции прибытия при необходимости
        if (arrivalStationId != null) {
            Station arrivalStation = stationRepository.findById(arrivalStationId).orElse(null);
            if (arrivalStation != null) {
                tickets = tickets.stream()
                        .filter(ticket -> ticket.getArrivalStation().getId().equals(arrivalStationId))
                        .collect(Collectors.toList());
            }
        }

        // Подсчет общего количества билетов
        long totalTickets = tickets.size();

        // Группировка билетов по маршрутам
        Map<Route, List<Ticket>> ticketsByRoute = tickets.stream()
                .collect(Collectors.groupingBy(ticket -> ticket.getSchedule().getRoute()));

        List<RouteStatistics> routeStatistics = new ArrayList<>();

        // Обработка каждого маршрута
        for (Map.Entry<Route, List<Ticket>> entry : ticketsByRoute.entrySet()) {
            Route route = entry.getKey();
            List<Ticket> routeTickets = entry.getValue();

            // Подсчет количества билетов для этого маршрута
            long ticketCount = routeTickets.size();

            // Получаем уникальные расписания для этого маршрута
            Set<Integer> uniqueScheduleIds = routeTickets.stream()
                    .map(ticket -> ticket.getSchedule().getId())
                    .collect(Collectors.toSet());

            // Подсчитываем общую вместимость всех задействованных поездов
            double totalCapacity = 0.0;
            for (Integer scheduleId : uniqueScheduleIds) {
                // Находим все билеты для этого расписания
                List<Ticket> scheduleTickets = routeTickets.stream()
                        .filter(ticket -> ticket.getSchedule().getId().equals(scheduleId))
                        .collect(Collectors.toList());

                if (!scheduleTickets.isEmpty()) {
                    // Берем первый билет, чтобы получить информацию о поезде
                    Ticket sampleTicket = scheduleTickets.get(0);

                    // Получаем вместимость поезда (количество вагонов * 50 мест в вагоне)
                    int trainCapacity = sampleTicket.getSchedule().getTrain().getTotalCars() * 50;

                    // Добавляем к общей вместимости
                    totalCapacity += trainCapacity;
                }
            }

            // Расчет процента занятости (обратите внимание на приведение к double для корректного деления)
            double occupancyRate = totalCapacity > 0 ? ((double)ticketCount / totalCapacity * 100.0) : 0;

            // Добавляем статистику по маршруту
            routeStatistics.add(new RouteStatistics(route.getName(), ticketCount, occupancyRate));
        }

        // Сортировка статистики по количеству билетов (в порядке убывания)
        routeStatistics.sort((a, b) -> Long.compare(b.getTicketCount(), a.getTicketCount()));

        // Возвращаем итоговую статистику
        return new TicketStatistics(totalTickets, 0.0, routeStatistics);
    }
}