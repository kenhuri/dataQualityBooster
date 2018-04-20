package com.micropole.dqb.service.impl;

import com.micropole.dqb.service.PythonService;
import com.micropole.dqb.domain.Python;
import com.micropole.dqb.repository.PythonRepository;
import com.micropole.dqb.repository.search.PythonSearchRepository;
import com.micropole.dqb.service.dto.PythonDTO;
import com.micropole.dqb.service.mapper.PythonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Python.
 */
@Service
@Transactional
public class PythonServiceImpl implements PythonService {

    private final Logger log = LoggerFactory.getLogger(PythonServiceImpl.class);

    private final PythonRepository pythonRepository;

    private final PythonMapper pythonMapper;

    private final PythonSearchRepository pythonSearchRepository;

    public PythonServiceImpl(PythonRepository pythonRepository, PythonMapper pythonMapper, PythonSearchRepository pythonSearchRepository) {
        this.pythonRepository = pythonRepository;
        this.pythonMapper = pythonMapper;
        this.pythonSearchRepository = pythonSearchRepository;
    }

    /**
     * Save a python.
     *
     * @param pythonDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PythonDTO save(PythonDTO pythonDTO) {
        log.debug("Request to save Python : {}", pythonDTO);
        Python python = pythonMapper.toEntity(pythonDTO);
        python = pythonRepository.save(python);
        PythonDTO result = pythonMapper.toDto(python);
        pythonSearchRepository.save(python);
        return result;
    }

    /**
     * Get all the pythons.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PythonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pythons");
        return pythonRepository.findAll(pageable)
            .map(pythonMapper::toDto);
    }

    /**
     * Get one python by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PythonDTO findOne(Long id) {
        log.debug("Request to get Python : {}", id);
        Python python = pythonRepository.findOne(id);
        return pythonMapper.toDto(python);
    }

    /**
     * Delete the python by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Python : {}", id);
        pythonRepository.delete(id);
        pythonSearchRepository.delete(id);
    }

    /**
     * Search for the python corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PythonDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Pythons for query {}", query);
        Page<Python> result = pythonSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(pythonMapper::toDto);
    }
}
