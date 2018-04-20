package com.micropole.dqb.service.impl;

import com.micropole.dqb.service.ContextService;
import com.micropole.dqb.domain.Context;
import com.micropole.dqb.repository.ContextRepository;
import com.micropole.dqb.repository.search.ContextSearchRepository;
import com.micropole.dqb.service.dto.ContextDTO;
import com.micropole.dqb.service.mapper.ContextMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Context.
 */
@Service
@Transactional
public class ContextServiceImpl implements ContextService {

    private final Logger log = LoggerFactory.getLogger(ContextServiceImpl.class);

    private final ContextRepository contextRepository;

    private final ContextMapper contextMapper;

    private final ContextSearchRepository contextSearchRepository;

    public ContextServiceImpl(ContextRepository contextRepository, ContextMapper contextMapper, ContextSearchRepository contextSearchRepository) {
        this.contextRepository = contextRepository;
        this.contextMapper = contextMapper;
        this.contextSearchRepository = contextSearchRepository;
    }

    /**
     * Save a context.
     *
     * @param contextDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContextDTO save(ContextDTO contextDTO) {
        log.debug("Request to save Context : {}", contextDTO);
        Context context = contextMapper.toEntity(contextDTO);
        context = contextRepository.save(context);
        ContextDTO result = contextMapper.toDto(context);
        contextSearchRepository.save(context);
        return result;
    }

    /**
     * Get all the contexts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContextDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contexts");
        return contextRepository.findAll(pageable)
            .map(contextMapper::toDto);
    }

    /**
     * Get one context by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ContextDTO findOne(Long id) {
        log.debug("Request to get Context : {}", id);
        Context context = contextRepository.findOne(id);
        return contextMapper.toDto(context);
    }

    /**
     * Delete the context by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Context : {}", id);
        contextRepository.delete(id);
        contextSearchRepository.delete(id);
    }

    /**
     * Search for the context corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContextDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Contexts for query {}", query);
        Page<Context> result = contextSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(contextMapper::toDto);
    }
}
