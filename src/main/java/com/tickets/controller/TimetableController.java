package com.tickets.controller;

import com.tickets.model.Timetable;
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

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * REST Controller responsible for handling timetable operations.
 */
@RestController
@RequestMapping("/timetables")
public class TimetableController {
    /**
     * Service for managing timetables.
     */
    private final TimetableService timetableService;

    /**
     * Constructor to inject the timetable service
     * @param timetableService
     */
    @Autowired
    public TimetableController(TimetableService timetableService){
        this.timetableService = timetableService;
    }

    @Operation(summary = "Add timetable")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Created timetable",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Timetable.class))
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
     * Add a new timetable.
     * @param timetable The timetable to add.
     * @return ResponseEntity with the added timetable
     */
    @PostMapping
    public ResponseEntity<Timetable> addTimetable(@RequestBody Timetable timetable) {
        try {
            timetable.setId(0L);
            timetable.setStartTime(timetable.getStartTime().truncatedTo(ChronoUnit.MINUTES));
            timetable.setEndTime(timetable.getEndTime().truncatedTo(ChronoUnit.MINUTES));
            Timetable addedTimetable = timetableService.addTimetable(timetable);
            return ResponseEntity.ok(addedTimetable);
        } catch (DataAccessException exception){
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Delete timetable by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted timetable",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Timetable not found",
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
     * Delete a timetable by its ID.
     * @param id The ID of the timetable to delete.
     * @return ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTimetableById(@PathVariable Long id) {
        try {
            if (timetableService.existTimetable(id)) {
                timetableService.deleteTimetableById(id);
                return ResponseEntity.ok("Timetable with ID" + id + " deleted successfully.");
            }
            return ResponseEntity.notFound().build();
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get timetable by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found timetable",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Timetable.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Timetable not found",
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
     * Retrieve a timetable by its ID.
     * @param id The ID of the timetable to retrieve.
     * @return ResponseEntity with the found timetable.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Timetable> getTimetableById(@PathVariable Long id){
        try {
            Timetable timetable = timetableService.getTimetableById(id);
            if (timetable != null) {
                return ResponseEntity.ok(timetable);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get all timetables")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found timetables",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Timetable.class))
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
     * Retrieve all timetables.
     * @return ResponseEntity with a list off all timetables.
     */
    @GetMapping
    public ResponseEntity<List<Timetable>> getAllTimetables() {
        try {
            List<Timetable> timetables = timetableService.getAllTimetables();
            return ResponseEntity.ok(timetables);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
