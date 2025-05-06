# Railway Ticket Booking System API

This Spring Boot application provides a comprehensive REST API for a railway ticket booking system based on the course project requirements of "Бази даних" (Databases) at Lviv Polytechnic National University.

## Features

- User authentication and authorization with JWT tokens
- Management of trains, stations, and routes
- Scheduling of train departures
- Ticket booking and management
- Seat availability tracking
- Statistics on route occupancy
- QR code generation for tickets
- Admin management interfaces

## Technology Stack

- **Backend Framework**: Spring Boot 3.2.0
- **Database**: MySQL
- **Security**: Spring Security with JWT
- **Documentation**: Swagger/OpenAPI 3.0
- **Additional Libraries**:
    - Lombok for reducing boilerplate code
    - Google ZXing for QR code generation
    - JUnit and Mockito for testing

## Project Structure

- **Entity Layer**: Database models with JPA annotations
- **Repository Layer**: JPA repositories for data access
- **Service Layer**: Business logic implementation
- **Controller Layer**: REST endpoints
- **DTO Layer**: Data transfer objects for API communication
- **Security Layer**: Authentication and authorization

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Users

- `GET /api/users` - Get all users (admin only)
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/me` - Get current user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user (admin only)

### Stations

- `GET /api/stations` - Get all stations
- `GET /api/stations/{id}` - Get station by ID
- `GET /api/stations/search?name={name}` - Search station by name
- `POST /api/stations` - Create station (admin only)
- `PUT /api/stations/{id}` - Update station (admin only)
- `DELETE /api/stations/{id}` - Delete station (admin only)

### Trains

- `GET /api/trains` - Get all trains
- `GET /api/trains/{id}` - Get train by ID
- `POST /api/trains` - Create train (admin only)
- `PUT /api/trains/{id}` - Update train (admin only)
- `DELETE /api/trains/{id}` - Delete train (admin only)

### Routes

- `GET /api/routes` - Get all routes
- `GET /api/routes/{id}` - Get route by ID
- `POST /api/routes` - Create route (admin only)
- `PUT /api/routes/{id}` - Update route (admin only)
- `DELETE /api/routes/{id}` - Delete route (admin only)
- `GET /api/routes/{id}/stations` - Get stations for a route
- `POST /api/routes/{routeId}/stations/{stationId}` - Add station to route (admin only)
- `DELETE /api/routes/{routeId}/stations/{stationId}` - Remove station from route (admin only)

### Schedules

- `GET /api/schedules` - Get all schedules
- `GET /api/schedules/{id}` - Get schedule by ID
- `POST /api/schedules?trainId={trainId}&routeId={routeId}&departureDate={date}` - Create schedule (admin only)
- `POST /api/schedules/{scheduleId}/stations/{routeStationId}` - Set station arrival time (admin only)
- `DELETE /api/schedules/{id}` - Delete schedule (admin only)
- `POST /api/schedules/search` - Search for routes by departure and arrival stations and date

### Tickets

- `GET /api/tickets` - Get all tickets (admin only)
- `GET /api/tickets/{id}` - Get ticket by ID
- `GET /api/tickets/number/{ticketNumber}` - Get ticket by number
- `GET /api/tickets/user/{userId}` - Get tickets for user
- `GET /api/tickets/me` - Get current user's tickets
- `POST /api/tickets` - Book a ticket
- `DELETE /api/tickets/{id}` - Cancel a ticket
- `GET /api/tickets/seats?scheduleId={scheduleId}&carNumber={carNumber}` - Get available seats
- `POST /api/tickets/statistics` - Get ticket statistics (admin only)

## Getting Started

### Prerequisites

- JDK 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository
2. Configure database connection in `application.properties`
3. Run the application:

```bash
mvn spring-boot:run
```

### API Documentation

After starting the application, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

## Sample Test Users

The system initializes with the following test users in development mode:

- **Admin User**:
    - Email: admin@example.com
    - Password: admin123
    - Role: ADMIN

- **Regular User**:
    - Email: user@example.com
    - Password: user123
    - Role: USER

## Database Schema

The system follows the database schema as defined in the provided DDL script, implementing the following tables:

- `users` - User accounts for passengers and administrators
- `stations` - Railway stations
- `trains` - Trains with car information
- `routes` - Train routes
- `route_stations` - Stations on a route with order and distance
- `schedules` - Train departures on specific dates
- `schedule_stations` - Arrival times for stations on a schedule
- `tickets` - Booked tickets with passenger and journey details

## License

This project is developed as a university course project.