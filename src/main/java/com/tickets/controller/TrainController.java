package com.tickets.controller;

import com.tickets.model.Train;
import com.tickets.service.TimetableService;
import com.tickets.service.TrainService;
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

import java.util.List;

/**
 * REST Controller responsible for handling train operations.
 */
@RestController
@RequestMapping("/trains")
public class TrainController {
    /**
     * Service for managing trains.
     */
    private final TrainService trainService;
    private final TimetableService timetableService;

    /**
     * Constructor to inject the train and timetable services.
     * @param trainService The train service.
     * @param timetableService The timetable service.
     */
    @Autowired
    public TrainController(TrainService trainService, TimetableService timetableService){
        this.trainService = trainService;
        this.timetableService = timetableService;
    }
    @Operation(summary = "Add train")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Created train",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Train.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Train with timetableID already exists",
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
     * Add a new train to timetable.
     * @param train The train to add.
     * @param timetableId The ID of the timetable to associate the train with.
     * @return ResponseEntity with the added train.
     */
    @PostMapping("/addTrainToTimetable/{timetableId}")
    public ResponseEntity<Train> addTrain(@RequestBody Train train, @PathVariable Long timetableId) {
        try {
            if (!timetableService.existTimetable(timetableId)){
                return ResponseEntity.notFound().build();
            }
            if (trainService.existTrainByTimetableID(timetableId)){
                return ResponseEntity.badRequest().build();
            }
            train.setId(0L);
            train.setTimetable(timetableService.getTimetableById(timetableId));
            Train addedTrain = trainService.addTrain(train);
            return ResponseEntity.ok(addedTrain);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Delete train by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted train",
                            content = @Content
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
     * Delete a train by its ID.
     * @param id The ID of the train to delete.
     * @return ResponseEntity indicating the result of the option.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrainById(@PathVariable Long id) {
        try {
            if (trainService.existTrain(id)) {
                trainService.deleteTrainById(id);
                return ResponseEntity.ok("Train with ID" + id + " deleted successfully.");
            }
            return ResponseEntity.notFound().build();
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get train by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found train",
                            content = @Content
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
     * Retrieve a train by its ID.
     * @param id The iD of the train to retrieve.
     * @return ResponseEntity with the found train.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(@PathVariable Long id) {
        try {
            Train train = trainService.getTrainById(id);
            if (train != null) {
                return ResponseEntity.ok(train);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get all trains")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found trains",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Train.class))
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
     * Retrieve all trains.
     * @return ResponseEntity with a list of all trains.
     */
    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains() {
        try {
            List<Train> trains = trainService.getAllTrains();
            return ResponseEntity.ok(trains);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
