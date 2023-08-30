package com.tickets.service;

import com.tickets.model.Reservation;
import com.tickets.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing reservations and related operations.
 */
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Creates new reservation.
     * @param reservation Reservation based on the provided reservation model.
     * @return The created reservation.
     */

    public Reservation addReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    /**
     * Deletes reservation by ID.
     * @param id The reservation ID to be canceled.
     */
    public void deleteReservationById(long id) {
        reservationRepository.deleteById(id);
    }

    /**
     * Retrieves reservation by ID.
     * @param id The reservation ID to be retrieved.
     * @return The reservation by ID.
     */
    public Reservation getReservationById(long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all reservations.
     * @return All reservations.
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Checks that reservation exists by ID.
     * @param id The reservation ID to check.
     * @return True if the reservation exist, False if not.
     */
    public boolean existReservation(long id) {
        return reservationRepository.existsById(id);
    }
}
