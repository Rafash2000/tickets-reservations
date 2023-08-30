package com.tickets.controller;

import com.tickets.model.Train;
import com.tickets.service.TimetableService;
import com.tickets.service.TrainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainControllerTest {
    @InjectMocks
    private TrainController trainController;

    @Mock
    private TrainService trainService;

    @Mock
    private TimetableService timetableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTrain() {
        Long timetableId = 1L;
        Train train = new Train();
        when(timetableService.existTimetable(timetableId)).thenReturn(true);
        when(trainService.existTrainByTimetableID(timetableId)).thenReturn(false);
        when(trainService.addTrain(train)).thenReturn(train);
        ResponseEntity<Train> response = trainController.addTrain(train, timetableId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(train, response.getBody());
    }

    @Test
    void deleteTrainById() {
        Long id = 1L;
        when(trainService.existTrain(id)).thenReturn(true);
        ResponseEntity<String> response = trainController.deleteTrainById(id);
        assertEquals(200, response.getStatusCodeValue());
        verify(trainService, times(1)).deleteTrainById(id);
    }

    @Test
    void getTrainById() {
        Long id = 1L;
        Train train = new Train();
        when(trainService.getTrainById(id)).thenReturn(train);
        ResponseEntity<Train> response = trainController.getTrainById(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(train, response.getBody());
    }

    @Test
    void getAllTrains() {
        List<Train> trains = Collections.singletonList(new Train());
        when(trainService.getAllTrains()).thenReturn(trains);
        ResponseEntity<List<Train>> response = trainController.getAllTrains();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(trains, response.getBody());
    }
}