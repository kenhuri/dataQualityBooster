package com.micropole.dqb.service;

import com.micropole.dqb.service.dto.ExecutionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Execution.
 */
public interface ExecutionService {

    /**
     * Save a execution.
     *
     * @param executionDTO the entity to save
     * @return the persisted entity
     */
    ExecutionDTO save(ExecutionDTO executionDTO);

    /**
     * Get all the executions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ExecutionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" execution.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ExecutionDTO findOne(Long id);

    /**
     * Delete the "id" execution.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the execution corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ExecutionDTO> search(String query, Pageable pageable);
}
