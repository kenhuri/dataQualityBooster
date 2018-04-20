package com.micropole.dqb.service;

import com.micropole.dqb.service.dto.PickleDTO;
import java.util.List;

/**
 * Service Interface for managing Pickle.
 */
public interface PickleService {

    /**
     * Save a pickle.
     *
     * @param pickleDTO the entity to save
     * @return the persisted entity
     */
    PickleDTO save(PickleDTO pickleDTO);

    /**
     * Get all the pickles.
     *
     * @return the list of entities
     */
    List<PickleDTO> findAll();

    /**
     * Get the "id" pickle.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PickleDTO findOne(Long id);

    /**
     * Delete the "id" pickle.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the pickle corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<PickleDTO> search(String query);
}
