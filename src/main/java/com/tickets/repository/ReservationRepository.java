package com.tickets.repository;

import com.tickets.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing reservations in the database.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}
