package com.micropole.dqb.service.impl;

import com.micropole.dqb.service.PickleService;
import com.micropole.dqb.domain.Pickle;
import com.micropole.dqb.repository.PickleRepository;
import com.micropole.dqb.repository.search.PickleSearchRepository;
import com.micropole.dqb.service.dto.PickleDTO;
import com.micropole.dqb.service.mapper.PickleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Pickle.
 */
@Service
@Transactional
public class PickleServiceImpl implements PickleService {

    private final Logger log = LoggerFactory.getLogger(PickleServiceImpl.class);

    private final PickleRepository pickleRepository;

    private final PickleMapper pickleMapper;

    private final PickleSearchRepository pickleSearchRepository;

    public PickleServiceImpl(PickleRepository pickleRepository, PickleMapper pickleMapper, PickleSearchRepository pickleSearchRepository) {
        this.pickleRepository = pickleRepository;
        this.pickleMapper = pickleMapper;
        this.pickleSearchRepository = pickleSearchRepository;
    }

    /**
     * Save a pickle.
     *
     * @param pickleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PickleDTO save(PickleDTO pickleDTO) {
        log.debug("Request to save Pickle : {}", pickleDTO);
        Pickle pickle = pickleMapper.toEntity(pickleDTO);
        pickle = pickleRepository.save(pickle);
        PickleDTO result = pickleMapper.toDto(pickle);
        pickleSearchRepository.save(pickle);
        return result;
    }

    /**
     * Get all the pickles.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PickleDTO> findAll() {
        log.debug("Request to get all Pickles");
        return pickleRepository.findAll().stream()
            .map(pickleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one pickle by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PickleDTO findOne(Long id) {
        log.debug("Request to get Pickle : {}", id);
        Pickle pickle = pickleRepository.findOne(id);
        return pickleMapper.toDto(pickle);
    }

    /**
     * Delete the pickle by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pickle : {}", id);
        pickleRepository.delete(id);
        pickleSearchRepository.delete(id);
    }

    /**
     * Search for the pickle corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PickleDTO> search(String query) {
        log.debug("Request to search Pickles for query {}", query);
        return StreamSupport
            .stream(pickleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(pickleMapper::toDto)
            .collect(Collectors.toList());
    }
}
