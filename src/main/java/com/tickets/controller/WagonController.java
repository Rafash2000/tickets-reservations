package com.tickets.controller;

import com.tickets.model.Wagon;
import com.tickets.service.TrainService;
import com.tickets.service.WagonService;
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
 * REST Controller responsible for handling wagon operations.
 */
@RestController
@RequestMapping("/wagons")
public class WagonController {
    /**
     * Service for managing wagons.
     */
    private final WagonService wagonService;
    private final TrainService trainService;

    /**
     * Constructor to inject the wagon and train services.
     * @param wagonService The wagon service.
     * @param trainService The train service.
     */
    @Autowired
    public WagonController(WagonService wagonService, TrainService trainService){
        this.wagonService = wagonService;
        this.trainService = trainService;

    }
    @Operation(summary = "Delete wagon by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted wagon",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Wagon not found",
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
     * Delete a wagon by its ID.
     * @param id The ID of the wagon to delete.
     * @return ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWagonById(@PathVariable Long id) {
        try {
            if (wagonService.existWagon(id)) {
                wagonService.deleteWagonById(id);
                return ResponseEntity.ok("Wagon with ID" + id + " deleted successfully.");
            }
            return ResponseEntity.notFound().build();
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Add a new wagon to a train.
     * @param wagon The wagon to add.
     * @param trainId The ID of the train to associate the wagon with.
     * @return ResponseEntity with the added wagon.
     */
    @Operation(summary = "Add wagon")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Created wagon",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Wagon.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Wagon not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Server error",
                            content = @Content
                    )
            }
    )
    @PostMapping("/addWagonToTrain/{trainId}")
    public ResponseEntity<Wagon> addWagon(@RequestBody Wagon wagon, @PathVariable Long trainId) {
        try {
            if (!trainService.existTrain(trainId)){
                return ResponseEntity.notFound().build();
            }
            wagon.setId(0L);
            wagon.setTrain(trainService.getTrainById(trainId));
            Wagon addedWagon = wagonService.addWagon(wagon);
            return ResponseEntity.ok(addedWagon);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get wagon by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found wagon",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Wagon.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Wagon not found",
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
     * Retrieve a wagon by its ID.
     * @param id The ID of the wagon to retrieve.
     * @return ResponseEntity with the found wagon.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Wagon> getWagonById(@PathVariable Long id){
        try {
            Wagon wagon = wagonService.getWagonById(id);
            if (wagon != null) {
                return ResponseEntity.ok(wagon);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get all wagons")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found wagons",
                            content = @Content(
                                    mediaType="application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Wagon.class))
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
     * Retrieve all wagons.
     * @return ResponseEntity with a list of all wagons
     */
    @GetMapping
    public ResponseEntity<List<Wagon>> getAllWagons() {
        try {
            List<Wagon> wagons = wagonService.getAllWagons();
            return ResponseEntity.ok(wagons);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
