package com.tickets.repository;

import com.tickets.model.Wagon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing wagons in the database.
 */
@Repository
public interface WagonRepository extends JpaRepository<Wagon, Long> {

}