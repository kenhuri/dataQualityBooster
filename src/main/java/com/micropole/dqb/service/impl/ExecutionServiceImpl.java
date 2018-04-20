package com.micropole.dqb.service.impl;

import com.micropole.dqb.service.ExecutionService;
import com.micropole.dqb.domain.Execution;
import com.micropole.dqb.repository.ExecutionRepository;
import com.micropole.dqb.repository.search.ExecutionSearchRepository;
import com.micropole.dqb.service.dto.ExecutionDTO;
import com.micropole.dqb.service.mapper.ExecutionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Execution.
 */
@Service
@Transactional
public class ExecutionServiceImpl implements ExecutionService {

    private final Logger log = LoggerFactory.getLogger(ExecutionServiceImpl.class);

    private final ExecutionRepository executionRepository;

    private final ExecutionMapper executionMapper;

    private final ExecutionSearchRepository executionSearchRepository;

    public ExecutionServiceImpl(ExecutionRepository executionRepository, ExecutionMapper executionMapper, ExecutionSearchRepository executionSearchRepository) {
        this.executionRepository = executionRepository;
        this.executionMapper = executionMapper;
        this.executionSearchRepository = executionSearchRepository;
    }

    /**
     * Save a execution.
     *
     * @param executionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ExecutionDTO save(ExecutionDTO executionDTO) {
        log.debug("Request to save Execution : {}", executionDTO);
        Execution execution = executionMapper.toEntity(executionDTO);
        execution = executionRepository.save(execution);
        ExecutionDTO result = executionMapper.toDto(execution);
        executionSearchRepository.save(execution);
        return result;
    }

    /**
     * Get all the executions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExecutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Executions");
        return executionRepository.findAll(pageable)
            .map(executionMapper::toDto);
    }

    /**
     * Get one execution by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExecutionDTO findOne(Long id) {
        log.debug("Request to get Execution : {}", id);
        Execution execution = executionRepository.findOne(id);
        return executionMapper.toDto(execution);
    }

    /**
     * Delete the execution by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Execution : {}", id);
        executionRepository.delete(id);
        executionSearchRepository.delete(id);
    }

    /**
     * Search for the execution corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExecutionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Executions for query {}", query);
        Page<Execution> result = executionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(executionMapper::toDto);
    }
}
