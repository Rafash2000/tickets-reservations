package com.tickets.repository;

import com.tickets.model.Timetable;
import com.tickets.model.enums.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Repository interface for managing timetables in the database.
 */

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    /**
     * Finds all StartStations in Timetable list.
     */
    List<Timetable> findAllByStartStation(Station startStation);

    /**
     * Finds all StartStations and EndStations in Timetable list.
     */
    List<Timetable> findAllByStartStationAndEndStation(Station startStation, Station endStation);

    /**
     * Finds all StartStations and EndStations with StartTime in Timetable list.
     */
    Timetable findByStartStationAndEndStationAndStartTime(Station startStation, Station endStation, LocalDateTime startTime);

}
