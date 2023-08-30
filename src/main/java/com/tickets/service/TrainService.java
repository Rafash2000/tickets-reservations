package com.tickets.service;

import com.tickets.model.Train;
import com.tickets.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for managing trains and related operations.
 */
@Service
public class TrainService {
    private final TrainRepository trainRepository;
    @Autowired
    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    /**
     * Creates new train.
     * @param train Create a new train based on the provided train model.
     * @return The created train.
     */
    public Train addTrain(Train train){return trainRepository.save(train);}

    /**
     * Deletes train by ID.
     * @param id The train ID to be canceled.
     */
    public void deleteTrainById(long id) {trainRepository.deleteById(id);}

    /**
     * Retrieves train by ID.
     * @param id The train ID to be retrieved.
     * @return The train by ID.
     */
    public Train getTrainById(long id) {
        return trainRepository.findById(id).orElse(null);
    }
    /**
     * Retrieves all trains.
     * @return All trains.
     */
    public List<Train> getAllTrains(){
        return trainRepository.findAll();
    }
    /**
     * Checks that train exists by ID.
     * @param id The train ID to check.
     * @return True if the train exist, False if not.
     */
    public boolean existTrain(long id) { return trainRepository.existsById(id); }
    /**
     * Checks that train exists by timeTable ID.
     * @param timetable The timeTable ID to check that train exists.
     * @return True if the train exists, False if not.
     */
    public boolean existTrainByTimetableID(long timetable) { return (trainRepository.findByTimetableId(timetable)!=null); }

}
