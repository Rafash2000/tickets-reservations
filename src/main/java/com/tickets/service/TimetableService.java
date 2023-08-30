package com.tickets.service;

import com.tickets.model.*;
import com.tickets.model.enums.Station;
import com.tickets.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing timetables and related operations.
 */
@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;
    @Autowired
    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    /**
     * Creates new timetable.
     * @param timetable Create a new timetable based on the provided timetable model.
     * @return The created timetable.
     */
    public Timetable addTimetable(Timetable timetable){
        return timetableRepository.save(timetable);
    }

    /**
     * Deletes timetable by ID.
     * @param id The timetable ID to be canceled.
     */
    public void deleteTimetableById(long id){
        timetableRepository.deleteById(id);
    }

    /**
     * Retrieves timetable by ID.
     * @param id The timetable ID to be retrieved.
     * @return The timetable by ID.
     */
    public Timetable getTimetableById(long id) {
        return timetableRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all timetables.
     * @return All timetables.
     */
    public List<Timetable> getAllTimetables(){
        return timetableRepository.findAll();
    }

    /**
     * Retrieves all timetables by StartStation.
     * @param startStation The startStation of the timetable for which all timetables are retrieved.
     * @return all timetables associated with the startStation.
     */
    public List<Timetable> getAllTimetablesByStartStation(Station startStation){
        return timetableRepository.findAllByStartStation(startStation);
    }

    /**
     * Retrieves all timetables by StartStation and EndStation.
     * @param startStation The startStation of the timetable for which all timetables are retrieved.
     * @param endStation The endStation of the timetable for which all timetables are retrieved.
     * @return All timetables associated with the startStation and endStation.
     */
    public List<Timetable> getAllTimetablesByStartStationAndEndStation(Station startStation, Station endStation){
        return timetableRepository.findAllByStartStationAndEndStation(startStation, endStation);
    }

    /**
     * Retrieves timetable by StartStation, EndStation and StartTime.
     * @param startStation The startStation of the timetable for which timetable is retrieved.
     * @param endStation The endStation of the timetable for which timetable is retrieved.
     * @param startTime The startTime of the timetable for which timetable is retrieved.
     * @return Timetable associated with the startStation, endStation and startTime.
     */
    public Timetable getTimetableByStartStationAndEndStationAndStartTime(Station startStation, Station endStation, LocalDateTime startTime){
        return timetableRepository.findByStartStationAndEndStationAndStartTime(startStation, endStation, startTime);
    }

    /**
     * Checks that timetable exists by ID.
     * @param id The timetable ID to check exists.
     * @return True if the timetable exists, False if not.
     */
    public boolean existTimetable(long id) { return timetableRepository.existsById(id); }

}