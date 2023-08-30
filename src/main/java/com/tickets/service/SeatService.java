package com.tickets.service;

import com.tickets.model.Seat;
import com.tickets.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing seats and related operations.
 */
@Service
public class SeatService {
    private final SeatRepository seatRepository;
    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Creates new seat.
     * @param seat Create a new seat based on the provided seat model.
     * @return The created seat.
     */
    public Seat addSeat(Seat seat){
        return seatRepository.save(seat);
    }

    /**
     * Deletes seat by ID.
     * @param id The seat ID to be canceled.
     */
    public void deleteSeatById(long id){
        seatRepository.deleteById(id);
    }

    /**
     * Retrieves seat by ID.
     * @param id The seat ID to be retrieved.
     * @return The seat by ID.
     */
    public Seat getSeatById(long id) {
        return seatRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all seats.
     * @return All seats.
     */
    public List<Seat> getAllSeats(){
        return seatRepository.findAll();
    }

    /**
     * Checks that seat exists by ID.
     * @param id The seat ID to check.
     * @return True if the seat exist, False if not.
     */
    public boolean existSeat(long id) { return seatRepository.existsById(id); }

    /**
     * Updates seat.
     * @param seat based on the provided seat model.
     * @return Updated seat.
     */
    public Seat updateSeat(Seat seat) { return seatRepository.save(seat); }

     /**
     * Retrieves list of seats by reservation ID.
     * @param reservationId The reservation ID to retrieve list of seats.
     * @return List of seats.
     */
    public List<Seat> getSeatsByReservationId(long reservationId) {return seatRepository.findSeatsByReservationId(reservationId); }
}
