package com.tickets.controller;

import com.tickets.model.Seat;
import com.tickets.model.Wagon;
import com.tickets.service.SeatService;
import com.tickets.service.WagonService;
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

class SeatControllerTest {
    @InjectMocks
    private SeatController seatController;

    @Mock
    private SeatService seatService;

    @Mock
    private WagonService wagonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSeat() {
        Long wagonId = 1L;
        Wagon wagon = new Wagon();
        Seat seat = new Seat();
        when(wagonService.existWagon(wagonId)).thenReturn(true);
        when(wagonService.getWagonById(wagonId)).thenReturn(wagon);
        when(seatService.addSeat(seat)).thenReturn(seat);
        ResponseEntity<Seat> response = seatController.addSeat(seat, wagonId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(seat, response.getBody());
    }

    @Test
    void deleteSeatById() {
        Long id = 1L;
        when(seatService.existSeat(id)).thenReturn(true);
        ResponseEntity<String> response = seatController.deleteSeatById(id);
        assertEquals(200, response.getStatusCodeValue());
        verify(seatService, times(1)).deleteSeatById(id);
    }

    @Test
    void getSeatById() {
        Long id = 1L;
        Seat seat = new Seat();
        when(seatService.getSeatById(id)).thenReturn(seat);
        ResponseEntity<Seat> response = seatController.getSeatById(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(seat, response.getBody());
    }

    @Test
    void getAllSeats() {
        List<Seat> seats = Collections.singletonList(new Seat());
        when(seatService.getAllSeats()).thenReturn(seats);
        ResponseEntity<List<Seat>> response = seatController.getAllSeats();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(seats, response.getBody());
    }
}