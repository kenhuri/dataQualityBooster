package com.micropole.dqb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.micropole.dqb.service.ContextService;
import com.micropole.dqb.web.rest.errors.BadRequestAlertException;
import com.micropole.dqb.web.rest.util.HeaderUtil;
import com.micropole.dqb.web.rest.util.PaginationUtil;
import com.micropole.dqb.service.dto.ContextDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Context.
 */
@RestController
@RequestMapping("/api")
public class ContextResource {

    private final Logger log = LoggerFactory.getLogger(ContextResource.class);

    private static final String ENTITY_NAME = "context";

    private final ContextService contextService;

    public ContextResource(ContextService contextService) {
        this.contextService = contextService;
    }

    /**
     * POST  /contexts : Create a new context.
     *
     * @param contextDTO the contextDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contextDTO, or with status 400 (Bad Request) if the context has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contexts")
    @Timed
    public ResponseEntity<ContextDTO> createContext(@RequestBody ContextDTO contextDTO) throws URISyntaxException {
        log.debug("REST request to save Context : {}", contextDTO);
        if (contextDTO.getId() != null) {
            throw new BadRequestAlertException("A new context cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContextDTO result = contextService.save(contextDTO);
        return ResponseEntity.created(new URI("/api/contexts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contexts : Updates an existing context.
     *
     * @param contextDTO the contextDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contextDTO,
     * or with status 400 (Bad Request) if the contextDTO is not valid,
     * or with status 500 (Internal Server Error) if the contextDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contexts")
    @Timed
    public ResponseEntity<ContextDTO> updateContext(@RequestBody ContextDTO contextDTO) throws URISyntaxException {
        log.debug("REST request to update Context : {}", contextDTO);
        if (contextDTO.getId() == null) {
            return createContext(contextDTO);
        }
        ContextDTO result = contextService.save(contextDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contextDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contexts : get all the contexts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contexts in body
     */
    @GetMapping("/contexts")
    @Timed
    public ResponseEntity<List<ContextDTO>> getAllContexts(Pageable pageable) {
        log.debug("REST request to get a page of Contexts");
        Page<ContextDTO> page = contextService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contexts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contexts/:id : get the "id" context.
     *
     * @param id the id of the contextDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contextDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contexts/{id}")
    @Timed
    public ResponseEntity<ContextDTO> getContext(@PathVariable Long id) {
        log.debug("REST request to get Context : {}", id);
        ContextDTO contextDTO = contextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contextDTO));
    }

    /**
     * DELETE  /contexts/:id : delete the "id" context.
     *
     * @param id the id of the contextDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contexts/{id}")
    @Timed
    public ResponseEntity<Void> deleteContext(@PathVariable Long id) {
        log.debug("REST request to delete Context : {}", id);
        contextService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contexts?query=:query : search for the context corresponding
     * to the query.
     *
     * @param query the query of the context search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/contexts")
    @Timed
    public ResponseEntity<List<ContextDTO>> searchContexts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Contexts for query {}", query);
        Page<ContextDTO> page = contextService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/contexts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
