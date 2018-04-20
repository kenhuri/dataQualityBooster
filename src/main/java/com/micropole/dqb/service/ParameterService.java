package com.micropole.dqb.service;

import com.micropole.dqb.service.dto.ParameterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Parameter.
 */
public interface ParameterService {

    /**
     * Save a parameter.
     *
     * @param parameterDTO the entity to save
     * @return the persisted entity
     */
    ParameterDTO save(ParameterDTO parameterDTO);

    /**
     * Get all the parameters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ParameterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" parameter.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ParameterDTO findOne(Long id);

    /**
     * Delete the "id" parameter.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the parameter corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ParameterDTO> search(String query, Pageable pageable);
}
