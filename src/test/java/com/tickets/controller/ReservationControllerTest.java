    package com.tickets.controller;

    import com.tickets.model.*;
    import com.tickets.model.enums.SeatType;
    import com.tickets.model.enums.Station;
    import com.tickets.service.ReservationService;
    import com.tickets.service.SeatService;
    import com.tickets.service.TimetableService;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;

    import java.time.LocalDateTime;
    import java.time.LocalTime;
    import java.time.format.DateTimeFormatter;
    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

    class ReservationControllerTest {

        private ReservationService reservationServiceMock;
        private ReservationController reservationController;
        private TimetableService timetableServiceMock;
        private SeatService seatServiceMock;
        private TimetableController timetableController;

        @BeforeEach
        void setUp() {
            timetableServiceMock = mock(TimetableService.class);
            seatServiceMock = mock(SeatService.class);

            reservationServiceMock = mock(ReservationService.class);
            reservationController = new ReservationController(reservationServiceMock, timetableServiceMock, seatServiceMock);
            timetableController = new TimetableController(timetableServiceMock);
        }

        @Test
        void testGetAllReservations() {
            Reservation reservation1 = new Reservation();
            Reservation reservation2 = new Reservation();
            List<Reservation> reservations = Arrays.asList(reservation1, reservation2);

            when(reservationServiceMock.getAllReservations()).thenReturn(reservations);

            ResponseEntity<List<Reservation>> response = reservationController.getAllReservations();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, response.getBody().size());
            verify(reservationServiceMock, times(1)).getAllReservations();
        }

        @Test
        void testGetReservationByIdWithValidId() {
            Long id = 1L;
            Reservation reservation = new Reservation();
            when(reservationServiceMock.getReservationById(id)).thenReturn(reservation);

            ResponseEntity<Reservation> response = reservationController.getReservationById(id);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(reservation, response.getBody());
            verify(reservationServiceMock, times(1)).getReservationById(id);
        }
        @Test
        void testDeleteReservationByIdWithValidId() {
            Long id = 1L;
            when(reservationServiceMock.existReservation(id)).thenReturn(true);

            ResponseEntity<String> response = reservationController.deleteReservationById(id);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().contains("deleted successfully"));
            verify(reservationServiceMock, times(1)).deleteReservationById(id);
        }

        @Test
        void testReserveSeats() {
            Station startStation = Station.Bydgoszcz;
            Station endStation = Station.Gdansk;
            LocalDateTime startTime = LocalDateTime.now();
            Integer numberOfSeats = 2;

            Timetable timetable = new Timetable();
            Seat seat1 = new Seat();
            Seat seat2 = new Seat();
            List<Seat> seats = Arrays.asList(seat1, seat2);

            Train train = new Train();
            Wagon wagon = new Wagon();
            wagon.setSeats(seats);
            train.setWagons(Arrays.asList(wagon));

            timetable.setTrain(train);

            TimetableService timetableServiceMock = mock(TimetableService.class);
            SeatService seatServiceMock = mock(SeatService.class);
            ReservationService reservationServiceMock = mock(ReservationService.class);

            when(timetableServiceMock.getTimetableByStartStationAndEndStationAndStartTime(startStation, endStation, startTime)).thenReturn(timetable);
            when(seatServiceMock.updateSeat(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));



            ReservationController reservationController = new ReservationController(reservationServiceMock, timetableServiceMock, seatServiceMock);

            ResponseEntity<List<Seat>> response = reservationController.reserveSeats(startStation, endStation, startTime, numberOfSeats);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, response.getBody().size());
            verify(reservationServiceMock, times(1)).addReservation(any(Reservation.class));
        }
        @Test
        void testGetEndStationsByStartStationWithValidData() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            Station startStation = Station.Bydgoszcz;
            Timetable timetable1 = new Timetable();
            timetable1.setEndStation(Station.Gdansk);
            timetable1.setStartTime(LocalDateTime.now());
            Timetable timetable2 = new Timetable();
            timetable2.setEndStation(Station.Katowice);
            timetable2.setStartTime(LocalDateTime.now().plusHours(1));
            List<Timetable> timetables = Arrays.asList(timetable1, timetable2);

            when(timetableServiceMock.getAllTimetablesByStartStation(any(Station.class))).thenReturn(timetables);

            ResponseEntity<List<String>> response = reservationController.getEndStationsByStartStation(startStation);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertTrue(response.getBody().contains(timetable1.getEndStation() + " " + timetable1.getStartTime().format(formatter)));
            assertTrue(response.getBody().contains(timetable2.getEndStation() + " " + timetable2.getStartTime().format(formatter)));
        }

        @Test
        public void testGetTimeTableByStartAndEndStationWithValidData() {
            // Given
            Station startStation = Station.Bydgoszcz;
            Station endStation = Station.Gdansk;
            Timetable timetable1 = new Timetable();
            Timetable timetable2 = new Timetable();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            timetable1.setStartStation(startStation);
            timetable1.setEndStation(endStation);
            timetable1.setStartTime(LocalDateTime.of(
                    2023, 8, 20, 15, 0));
            timetable1.setEndTime(LocalDateTime.of(
                    2023, 8, 20, 17, 0));

            timetable2.setStartStation(startStation);
            timetable2.setEndStation(endStation);
            timetable2.setStartTime(LocalDateTime.of(
                    2023, 8, 20, 20, 0));
            timetable2.setEndTime(LocalDateTime.of(
                    2023, 8, 20, 22, 0));

            when(timetableServiceMock.getAllTimetablesByStartStationAndEndStation(startStation, endStation))
                    .thenReturn(Arrays.asList(timetable1, timetable2));


            ResponseEntity<List<String>> response = reservationController.getTimeTableByStartAndEndStation(startStation, endStation);

            assertEquals(200, response.getStatusCodeValue());
            assertTrue(response.getBody().contains(startStation + ": " + timetable1.getStartTime().format(formatter) + ", " + endStation + " " + timetable1.getEndTime().format(formatter)));
            assertTrue(response.getBody().contains(startStation + ": " + timetable2.getStartTime().format(formatter) + ", " + endStation + " " + timetable2.getEndTime().format(formatter)));
        }

        @Test
        public void testReserveSeat() {
            Station startStation = Station.Bydgoszcz;
            Station endStation = Station.Gdansk;
            LocalDateTime startTime = LocalDateTime.of(2023, 8, 20, 15, 0);
            SeatType seatType = SeatType.Corridor;

            Timetable timetable = mock(Timetable.class);
            Train train = mock(Train.class);
            Wagon wagon = mock(Wagon.class);
            Seat seat1 = new Seat();
            Seat seat2 = new Seat();
            seat1.setSeatType(seatType);
            seat2.setSeatType(seatType);
            Reservation reservation = new Reservation();

            when(timetableServiceMock.getTimetableByStartStationAndEndStationAndStartTime(startStation, endStation, startTime)).thenReturn(timetable);
            when(timetable.getTrain()).thenReturn(train);
            when(train.getWagons()).thenReturn(Collections.singletonList(wagon));
            when(wagon.getSeats()).thenReturn(Arrays.asList(seat1, seat2));
            when(seatServiceMock.existSeat(anyLong())).thenReturn(true);
            when(reservationServiceMock.addReservation(any(Reservation.class))).thenReturn(reservation);
            when(seatServiceMock.updateSeat(any(Seat.class))).thenReturn(seat2); // this can be adjusted based on the actual implementation

            ResponseEntity<Seat> response = reservationController.reserveSeat(startStation, endStation, startTime, seatType);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(seatType, response.getBody().getSeatType());
            assertNotNull(response.getBody().getReservation());
        }
    }

