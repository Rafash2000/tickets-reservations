package com.tickets.repository;

import com.tickets.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for managing seats in the database.
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    /**
     * Finds a seats by reservation ID.
     */
    List<Seat> findSeatsByReservationId(long reservationId);


}
