package com.tickets.controller;

import com.tickets.model.Seat;
import com.tickets.service.SeatService;
import com.tickets.service.WagonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller responsible for handling seat operations.
 */
@ComponentScan
@RestController
@RequestMapping("/seats")
public class SeatController {
    /**
     * Service for managing seats.
     */
    private final SeatService seatService;
    /**
     * Service for managing wagons.
     */
    private final WagonService wagonService;

    /**
     * Constructor to inject necessary services.
     * @param seatService The seat service.
     * @param wagonService The wagon service.
     */
    @Autowired
    public SeatController(SeatService seatService,WagonService wagonService){
        this.seatService = seatService;
        this.wagonService = wagonService;
    }
    @Operation(summary = "Add seat")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Created seat",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Seat.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Train not found",
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
     * Add a new seat to a specific wagon.
     * @param seat The seat to add.
     * @param wagonId the ID of the wagon to which the seat will be added.
     * @return ResponseEntity with the added seat.
     */
    @PostMapping("/addSeatToWagon/{wagonId}")
    public ResponseEntity<Seat> addSeat(@RequestBody Seat seat, @PathVariable Long wagonId) {
        try {
            if (!wagonService.existWagon(wagonId)){
                return ResponseEntity.notFound().build();
            }
            seat.setId(0L);
            seat.setWagon(wagonService.getWagonById(wagonId));
            Seat addedSeat = seatService.addSeat(seat);
            return ResponseEntity.ok(addedSeat);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Deleted seat by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted seat",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Seat not found",
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
     * Delete a seat by its ID.
     * @param id The ID of the seat to delete.
     * @return ResponseEntity indicating the result of the operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeatById(@PathVariable Long id) {
        try {
            if (seatService.existSeat(id)){
                seatService.deleteSeatById(id);
                return ResponseEntity.ok("Seat with ID" + id + " deleted successfully.");
            }
            return ResponseEntity.notFound().build();
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Find seat by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found seat",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Seat.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Seat not found",
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
     * Retrieve a seat by its ID.
     * @param id The ID of the seat to retrieve.
     * @return ResponseEntity with the found seat.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Long id){
        try {
            Seat seat = seatService.getSeatById(id);
            if (seat != null) {
                return ResponseEntity.ok(seat);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get all seats")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found seats",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Seat.class))
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
     * Retrieve all seats.
     * @return ResponseEntity with a list of all seats.
     */
    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        try {
            List<Seat> seats = seatService.getAllSeats();
            return ResponseEntity.ok(seats);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
