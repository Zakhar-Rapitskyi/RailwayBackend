package com.rapitskyi.railwayapplication.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rapitskyi.railwayapplication.dto.SearchDTOs.*;
import com.rapitskyi.railwayapplication.dto.TicketDTO;
import com.rapitskyi.railwayapplication.entity.*;
import com.rapitskyi.railwayapplication.exception.CustomExceptions;
import com.rapitskyi.railwayapplication.repository.*;
//import com.rapitskyi.railwayapplication.exception.CustomExceptions.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final StationRepository stationRepository;
    private final RouteStationRepository routeStationRepository;
    private final ScheduleStationRepository scheduleStationRepository;
    private final UserService userService;

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public TicketDTO getTicketById(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Ticket not found with id: " + id));
        return TicketDTO.fromEntity(ticket);
    }

    public TicketDTO getTicketByNumber(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Ticket not found with number: " + ticketNumber));
        return TicketDTO.fromEntity(ticket);
    }

    public List<TicketDTO> getTicketsByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("User not found with id: " + userId));
        
        return ticketRepository.findByUser(user).stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TicketDTO> getCurrentUserTickets() {
        User currentUser = userService.getCurrentUser().toEntity();
        return ticketRepository.findByUser(currentUser).stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketDTO bookTicket(TicketBookingRequest request) {
        User currentUser = userService.getCurrentUser().toEntity();

        if (request.getScheduleId() == null || request.getDepartureStationId() == null || 
            request.getArrivalStationId() == null || request.getCarNumber() == null || 
            request.getSeatNumber() == null) {
            throw new CustomExceptions.BadRequestException("All fields are required");
        }

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Schedule not found with id: " + request.getScheduleId()));
                
        Station departureStation = stationRepository.findById(request.getDepartureStationId())
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Departure station not found with id: " + request.getDepartureStationId()));
                
        Station arrivalStation = stationRepository.findById(request.getArrivalStationId())
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Arrival station not found with id: " + request.getArrivalStationId()));

        RouteStation departureRouteStation = routeStationRepository
                .findByRouteIdAndStationId(schedule.getRoute().getId(), departureStation.getId())
                .orElseThrow(() -> new CustomExceptions.BadRequestException("Departure station is not on this route"));
                
        RouteStation arrivalRouteStation = routeStationRepository
                .findByRouteIdAndStationId(schedule.getRoute().getId(), arrivalStation.getId())
                .orElseThrow(() -> new CustomExceptions.BadRequestException("Arrival station is not on this route"));

        if (departureRouteStation.getStationOrder() >= arrivalRouteStation.getStationOrder()) {
            throw new CustomExceptions.BadRequestException("Departure station must come before arrival station on the route");
        }

        ScheduleStation departureScheduleStation = scheduleStationRepository
                .findByScheduleAndRouteStation(schedule, departureRouteStation)
                .orElseThrow(() -> new CustomExceptions.BadRequestException("Schedule time not set for departure station"));
                
        ScheduleStation arrivalScheduleStation = scheduleStationRepository
                .findByScheduleAndRouteStation(schedule, arrivalRouteStation)
                .orElseThrow(() -> new CustomExceptions.BadRequestException("Schedule time not set for arrival station"));

        if (request.getCarNumber() <= 0 || request.getCarNumber() > schedule.getTrain().getTotalCars()) {
            throw new CustomExceptions.BadRequestException("Invalid car number");
        }
        
        if (request.getSeatNumber() <= 0 || request.getSeatNumber() > 50) {
            throw new CustomExceptions.BadRequestException("Invalid seat number");
        }

        ticketRepository.findByScheduleIdAndCarNumberAndSeatNumber(
                schedule.getId(), request.getCarNumber(), request.getSeatNumber())
                .ifPresent(t -> {
                    throw new CustomExceptions.BadRequestException("This seat is already booked");
                });
        
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(generateTicketNumber());
        ticket.setUser(currentUser);
        ticket.setSchedule(schedule);
        ticket.setCarNumber(request.getCarNumber());
        ticket.setSeatNumber(request.getSeatNumber());
        ticket.setDepartureStation(departureStation);
        ticket.setArrivalStation(arrivalStation);

        LocalDate departureDate = schedule.getDepartureDate();
        LocalTime departureTime = departureScheduleStation.getArrivalTime();
        LocalTime arrivalTime = arrivalScheduleStation.getArrivalTime();
        
        ticket.setDepartureDatetime(LocalDateTime.of(departureDate, departureTime));
        ticket.setArrivalDatetime(LocalDateTime.of(departureDate, arrivalTime));

        try {
            ticket.setQrCode(generateQRCode(ticket.getTicketNumber()));
        } catch (Exception e) {
            ticket.setQrCode(null);
        }
        
        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketDTO.fromEntity(savedTicket);
    }

    @Transactional
    public void cancelTicket(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Ticket not found with id: " + id));

        User currentUser = userService.getCurrentUser().toEntity();
        if (!ticket.getUser().getId().equals(currentUser.getId()) && currentUser.getRole() != User.UserRole.admin) {
            throw new CustomExceptions.BadRequestException("You can only cancel your own tickets");
        }

        if (ticket.getDepartureDatetime().isBefore(LocalDateTime.now())) {
            throw new CustomExceptions.BadRequestException("Cannot cancel a ticket for a past departure");
        }
        
        ticketRepository.delete(ticket);
    }

    public CarSeatInfo getAvailableSeats(Integer scheduleId, Integer carNumber) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Schedule not found with id: " + scheduleId));
        
        if (carNumber <= 0 || carNumber > schedule.getTrain().getTotalCars()) {
            throw new CustomExceptions.BadRequestException("Invalid car number");
        }
        
        List<Integer> occupiedSeats = ticketRepository.findBookedSeatsByScheduleIdAndCarNumber(scheduleId, carNumber);
        
        return new CarSeatInfo(carNumber, occupiedSeats, 50);
    }

    private String generateTicketNumber() {
        return "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateQRCode(String data) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}