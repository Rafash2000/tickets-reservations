package com.tickets.controller;

import com.tickets.model.*;
import com.tickets.model.enums.SeatType;
import com.tickets.model.enums.Station;
import com.tickets.service.ReservationService;
import com.tickets.service.SeatService;
import com.tickets.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest Controller responsible for handling reservations.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    /**
     * Service for managing reservations.
     */
    private final ReservationService reservationService;
    /**
     * Service for managing timetables.
     */
    private final TimetableService timetableService;
    /**
     * Service for managing seats.
     */
    private final SeatService seatService;
    /**
     * Date and time formatter for consistent date-time formatting.
     */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    /**
     * Constructor to inject necessary services.
     * @param reservationService The reservationService.
     * @param timetableService The timetableService.
     * @param seatService The seatService.
     */

    @Autowired
    public ReservationController(ReservationService reservationService,
                                 TimetableService timetableService,
                                 SeatService seatService) {

        this.reservationService = reservationService;
        this.timetableService = timetableService;
        this.seatService = seatService;
    }

    @Operation(summary = "Get possible connections by start station")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found possible connections",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Connections not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Server error",
                            content = @Content
                    )
            }
    )
    /**
     * Retrieve possible end stations based on the start station.
     * @param startStation The starting station.
     * @return ResponseEntity with a list of end stations and dates.
     */
    @GetMapping("/endStations/{startStation}")
    public ResponseEntity<List<String>> getEndStationsByStartStation(@PathVariable Station startStation) {
        try {
            List<Timetable> timetables = timetableService.getAllTimetablesByStartStation(startStation);
            if (timetables.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<String> datesAndEndStationsForStartStation = new ArrayList<>();

            for (Timetable timetable: timetables) {
                String formattedTime = timetable.getStartTime().format(formatter);
                datesAndEndStationsForStartStation.add(timetable.getEndStation()+" "+formattedTime);
            }
            return ResponseEntity.ok(datesAndEndStationsForStartStation);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get possible connections between stations")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found connections",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Connections not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Server error",
                            content = @Content
                    )
            }
    )
    /**
     * Retrieve possible connections between stations.
     * @param startStation The starting station.
     * @param endStation The ending station.
     * @return ResponseEntity with a list of connections
     */
    @GetMapping("/startAndEndStation/{startStation}/{endStation}")
    public ResponseEntity<List<String>> getTimeTableByStartAndEndStation(@PathVariable Station startStation, @PathVariable Station endStation) {
        try {
            var timetablesToEndStation = timetableService.getAllTimetablesByStartStationAndEndStation(startStation,endStation);
            if (timetablesToEndStation.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<String> datesAndEndStationsForStartStation = new ArrayList<>();
            timetablesToEndStation
                    .forEach(timetable -> {
                        var startTime = timetable.getStartTime().format(formatter);
                        var endTime = timetable.getEndTime().format(formatter);
                        datesAndEndStationsForStartStation.add(
                                timetable.getStartStation() + ": " + startTime + ", "
                                        + timetable.getEndStation() + " " + endTime);
                    });
            return ResponseEntity.ok(datesAndEndStationsForStartStation);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @SuppressWarnings("ReassignedVariable")
    @Operation(summary = "Reserve seats (at least 2)")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reserved seats",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Seat.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Connection not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Wrong number or not enough of not reserved seats",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Server error",
                            content = @Content
                    )
            }
    )
    /**
     * Reserve seats for a given journey.
     *
     * @param startStation  The starting station.
     * @param endStation    The ending station.
     * @param startTime     The journey start time.
     * @param numberOfSeats The number of seats to reserve.
     * @return ResponseEntity with a list of reserved seats.
     */
    @PostMapping("/startAndEndStation/{startStation}/{endStation}/{startTime}/numberOfSeats/{numberOfSeats}")
    public ResponseEntity<List<Seat>> reserveSeats(
            @PathVariable Station startStation, @PathVariable Station endStation,
            @PathVariable LocalDateTime startTime,@PathVariable Integer numberOfSeats){
        try {
            if (numberOfSeats <2) return ResponseEntity.badRequest().build();

            Timetable timetable = timetableService.getTimetableByStartStationAndEndStationAndStartTime(startStation, endStation, startTime);
            if (timetable==null) {
                return ResponseEntity.notFound().build();
            }

            List<Wagon> wagons = timetable.getTrain().getWagons();
            List<Seat> seats = new ArrayList<>();
            for (Wagon wagon: wagons) {
                seats.addAll(wagon.getSeats());
            }
            seats.removeIf(seat -> seat.getReservation()!=null);

            if (numberOfSeats > seats.size()) {
                return ResponseEntity.badRequest().build();
            }

            if (numberOfSeats < seats.size()){
                seats = seats.subList(0,numberOfSeats);
            }

            Reservation addedReservation = reservationService.addReservation(new Reservation());

            for (Seat seat: seats) {
                seat.setReservation(addedReservation);
                seatService.updateSeat(seat);
            }

            return ResponseEntity.ok(seats);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Reserve seat (only 1)")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reserved seat",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Seat.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Connection not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Not reserved seat is not available",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Server error",
                            content = @Content
                    )
            }
    )
    /**
     * Reserve type of seat for a given journey.
     *
     * @param startStation  The starting station.
     * @param endStation    The ending station.
     * @param startTime     The journey start time.
     * @param seatType The type of seat to reserve.
     * @return ResponseEntity with a type of reserved seat.
     */
    @PostMapping("/startAndEndStation/{startStation}/{endStation}/{startTime}/seatType/{seatType}")
    public ResponseEntity<Seat> reserveSeat(
            @PathVariable Station startStation, @PathVariable Station endStation,
            @PathVariable LocalDateTime startTime,@PathVariable SeatType seatType){
        try {

            Timetable timetable = timetableService.getTimetableByStartStationAndEndStationAndStartTime(startStation, endStation, startTime);
            if (timetable==null) {
                return ResponseEntity.notFound().build();
            }

            List<Wagon> wagons = timetable.getTrain().getWagons();
            List<Seat> seats = new ArrayList<>();
            for (Wagon wagon: wagons) {
                seats.addAll(wagon.getSeats());
            }
            seats.removeIf(seat -> seat.getReservation()!=null);
            if (seats.isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            Seat notReservedSeat = new Seat();
            for (Seat seat: seats) {
                if (seat.getSeatType().equals(seatType)){
                    notReservedSeat = seat;

                    break;
                }
            }
            if (!seatService.existSeat(notReservedSeat.getId())) {
                notReservedSeat = seats.get(0);
            }

            Reservation addedReservation = reservationService.addReservation(new Reservation());
            notReservedSeat.setReservation(addedReservation);
            seatService.updateSeat(notReservedSeat);

            return ResponseEntity.ok(notReservedSeat);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Delete reservation by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted reservation",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Reservation not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Server error",
                            content = @Content
                    )
            }
    )
   /**
     * Delete reservation by ID.
     * @param reservationId
     * @return ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> deleteReservationById(@PathVariable Long reservationId) {
        try {
            if (!reservationService.existReservation(reservationId)) {
                return ResponseEntity.notFound().build();
            }
            List<Seat> reservedSeats = seatService.getSeatsByReservationId(reservationId);
            for (Seat seat: reservedSeats) {
                seat.setReservation(null);
                seatService.updateSeat(seat);
            }
            reservationService.deleteReservationById(reservationId);
            return ResponseEntity.ok("Reservation with ID" + reservationId + " deleted successfully.");
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get reservation by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found reservation",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Reservation.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Reservation not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Server error",
                            content = @Content
                    )
            }
    )
    /**
     * Retrieve a reservation by ID.
     * @param id The ID of the reservation.
     * @return ResponseEntity with the found reservation.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.getReservationById(id);
            if (reservation != null) {
                return ResponseEntity.ok(reservation);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get all reservation")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Created reservation",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Reservation.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Server error",
                            content = @Content
                    )
            }
    )
    /**
     * Retrieve all reservations.
     * @return ResponseEntity with a list of all reservations.
     */
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            return ResponseEntity.ok(reservations);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

