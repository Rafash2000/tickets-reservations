package com.tickets.controller;

import com.tickets.model.Timetable;
import com.tickets.service.TimetableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TimetableControllerTest {
    @InjectMocks
    private TimetableController timetableController;

    @Mock
    private TimetableService timetableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTimetable() {
        Timetable timetable = new Timetable();
        timetable.setStartTime(LocalDateTime.now());
        timetable.setEndTime(LocalDateTime.now().plusHours(1));
        when(timetableService.addTimetable(timetable)).thenReturn(timetable);
        ResponseEntity<Timetable> response = timetableController.addTimetable(timetable);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(timetable, response.getBody());
    }

    @Test
    void deleteTimetableById() {
        Long id = 1L;
        when(timetableService.existTimetable(id)).thenReturn(true);
        ResponseEntity<String> response = timetableController.deleteTimetableById(id);
        assertEquals(200, response.getStatusCodeValue());
        verify(timetableService, times(1)).deleteTimetableById(id);
    }

    @Test
    void getTimetableById() {
        Long id = 1L;
        Timetable timetable = new Timetable();
        when(timetableService.getTimetableById(id)).thenReturn(timetable);
        ResponseEntity<Timetable> response = timetableController.getTimetableById(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(timetable, response.getBody());
    }

    @Test
    void getAllTimetables() {
        List<Timetable> timetables = Collections.singletonList(new Timetable());
        when(timetableService.getAllTimetables()).thenReturn(timetables);
        ResponseEntity<List<Timetable>> response = timetableController.getAllTimetables();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(timetables, response.getBody());
    }
}