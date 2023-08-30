package com.tickets.repository;

import com.tickets.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing trains in the database.
 */

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    /**
     * Finds a train by timetable ID.
     */
    Train findByTimetableId(long timetableId);
}
