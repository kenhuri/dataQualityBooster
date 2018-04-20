package com.micropole.dqb.service.impl;

import com.micropole.dqb.service.ParameterService;
import com.micropole.dqb.domain.Parameter;
import com.micropole.dqb.repository.ParameterRepository;
import com.micropole.dqb.repository.search.ParameterSearchRepository;
import com.micropole.dqb.service.dto.ParameterDTO;
import com.micropole.dqb.service.mapper.ParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Parameter.
 */
@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    private final Logger log = LoggerFactory.getLogger(ParameterServiceImpl.class);

    private final ParameterRepository parameterRepository;

    private final ParameterMapper parameterMapper;

    private final ParameterSearchRepository parameterSearchRepository;

    public ParameterServiceImpl(ParameterRepository parameterRepository, ParameterMapper parameterMapper, ParameterSearchRepository parameterSearchRepository) {
        this.parameterRepository = parameterRepository;
        this.parameterMapper = parameterMapper;
        this.parameterSearchRepository = parameterSearchRepository;
    }

    /**
     * Save a parameter.
     *
     * @param parameterDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ParameterDTO save(ParameterDTO parameterDTO) {
        log.debug("Request to save Parameter : {}", parameterDTO);
        Parameter parameter = parameterMapper.toEntity(parameterDTO);
        parameter = parameterRepository.save(parameter);
        ParameterDTO result = parameterMapper.toDto(parameter);
        parameterSearchRepository.save(parameter);
        return result;
    }

    /**
     * Get all the parameters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ParameterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parameters");
        return parameterRepository.findAll(pageable)
            .map(parameterMapper::toDto);
    }

    /**
     * Get one parameter by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ParameterDTO findOne(Long id) {
        log.debug("Request to get Parameter : {}", id);
        Parameter parameter = parameterRepository.findOne(id);
        return parameterMapper.toDto(parameter);
    }

    /**
     * Delete the parameter by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Parameter : {}", id);
        parameterRepository.delete(id);
        parameterSearchRepository.delete(id);
    }

    /**
     * Search for the parameter corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ParameterDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Parameters for query {}", query);
        Page<Parameter> result = parameterSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(parameterMapper::toDto);
    }
}
