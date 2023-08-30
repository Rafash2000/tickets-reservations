package com.tickets.controller;

import com.tickets.model.Wagon;
import com.tickets.service.TrainService;
import com.tickets.service.WagonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


class WagonControllerTest {
    @InjectMocks
    private WagonController wagonController;

    @Mock
    private WagonService wagonService;

    @Mock
    private TrainService trainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void deleteWagonById() {
        Long id = 1L;
        when(wagonService.existWagon(id)).thenReturn(true);
        ResponseEntity<String> response = wagonController.deleteWagonById(id);
        assertEquals(200, response.getStatusCodeValue());
        verify(wagonService, times(1)).deleteWagonById(id);
    }


    @Test
    void addWagon() {
        Long trainId = 1L;
        Wagon wagon = new Wagon();
        when(trainService.existTrain(trainId)).thenReturn(true);
        when(wagonService.addWagon(wagon)).thenReturn(wagon);
        ResponseEntity<Wagon> response = wagonController.addWagon(wagon, trainId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(wagon, response.getBody());
    }


    @Test
    void getWagonById() {
        Long id = 1L;
        Wagon wagon = new Wagon();
        when(wagonService.getWagonById(id)).thenReturn(wagon);
        ResponseEntity<Wagon> response = wagonController.getWagonById(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(wagon, response.getBody());
    }

    @Test
    void getAllWagons() {
        List<Wagon> wagons = Collections.singletonList(new Wagon());
        when(wagonService.getAllWagons()).thenReturn(wagons);
        ResponseEntity<List<Wagon>> response = wagonController.getAllWagons();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(wagons, response.getBody());
    }

}