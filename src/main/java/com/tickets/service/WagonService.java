package com.tickets.service;

import com.tickets.model.Wagon;
import com.tickets.repository.WagonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service class for managing wagons and related operations.
 */
@Service
public class WagonService {
    private final WagonRepository wagonRepository;
    @Autowired
    public WagonService(WagonRepository wagonRepository){
        this.wagonRepository = wagonRepository;
    }

    /**
     * Creates new wagon.
     * @param wagon Create a new wagon based on the provided wagon model.
     * @return The created wagon.
     */
    public Wagon addWagon(Wagon wagon){
        return wagonRepository.save(wagon);
    }

    /**
     * Deletes wagon by ID.
     * @param id The wagon ID to be canceled.
     */
    public void deleteWagonById(long id){
        wagonRepository.deleteById(id);
    }

    /**
     * Retrieves wagon by ID.
     * @param id The wagon ID to be retrieved.
     * @return The wagon by ID.
     */
    public  Wagon getWagonById(Long id) {return wagonRepository.findById(id).orElse(null);}

    /**
     * Retrieves all wagons.
     * @return All wagons.
     */
    public List<Wagon> getAllWagons(){
        return wagonRepository.findAll();
    }

    /**
     * Checks that wagon exists by ID.
     * @param id The wagon ID to check.
     * @return True if the wagon exist, False if not.
     */
    public boolean existWagon(long id) { return wagonRepository.existsById(id); }
}
