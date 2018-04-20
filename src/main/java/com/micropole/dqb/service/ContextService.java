package com.micropole.dqb.service;

import com.micropole.dqb.service.dto.ContextDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Context.
 */
public interface ContextService {

    /**
     * Save a context.
     *
     * @param contextDTO the entity to save
     * @return the persisted entity
     */
    ContextDTO save(ContextDTO contextDTO);

    /**
     * Get all the contexts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContextDTO> findAll(Pageable pageable);

    /**
     * Get the "id" context.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ContextDTO findOne(Long id);

    /**
     * Delete the "id" context.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the context corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContextDTO> search(String query, Pageable pageable);
}
