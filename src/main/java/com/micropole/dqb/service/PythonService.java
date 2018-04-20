package com.micropole.dqb.service;

import com.micropole.dqb.service.dto.PythonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Python.
 */
public interface PythonService {

    /**
     * Save a python.
     *
     * @param pythonDTO the entity to save
     * @return the persisted entity
     */
    PythonDTO save(PythonDTO pythonDTO);

    /**
     * Get all the pythons.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PythonDTO> findAll(Pageable pageable);

    /**
     * Get the "id" python.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PythonDTO findOne(Long id);

    /**
     * Delete the "id" python.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the python corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PythonDTO> search(String query, Pageable pageable);
}
