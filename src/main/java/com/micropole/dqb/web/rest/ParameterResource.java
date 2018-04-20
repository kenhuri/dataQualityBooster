package com.micropole.dqb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.micropole.dqb.service.ParameterService;
import com.micropole.dqb.web.rest.errors.BadRequestAlertException;
import com.micropole.dqb.web.rest.util.HeaderUtil;
import com.micropole.dqb.web.rest.util.PaginationUtil;
import com.micropole.dqb.service.dto.ParameterDTO;
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
 * REST controller for managing Parameter.
 */
@RestController
@RequestMapping("/api")
public class ParameterResource {

    private final Logger log = LoggerFactory.getLogger(ParameterResource.class);

    private static final String ENTITY_NAME = "parameter";

    private final ParameterService parameterService;

    public ParameterResource(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    /**
     * POST  /parameters : Create a new parameter.
     *
     * @param parameterDTO the parameterDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new parameterDTO, or with status 400 (Bad Request) if the parameter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/parameters")
    @Timed
    public ResponseEntity<ParameterDTO> createParameter(@RequestBody ParameterDTO parameterDTO) throws URISyntaxException {
        log.debug("REST request to save Parameter : {}", parameterDTO);
        if (parameterDTO.getId() != null) {
            throw new BadRequestAlertException("A new parameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParameterDTO result = parameterService.save(parameterDTO);
        return ResponseEntity.created(new URI("/api/parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /parameters : Updates an existing parameter.
     *
     * @param parameterDTO the parameterDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated parameterDTO,
     * or with status 400 (Bad Request) if the parameterDTO is not valid,
     * or with status 500 (Internal Server Error) if the parameterDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/parameters")
    @Timed
    public ResponseEntity<ParameterDTO> updateParameter(@RequestBody ParameterDTO parameterDTO) throws URISyntaxException {
        log.debug("REST request to update Parameter : {}", parameterDTO);
        if (parameterDTO.getId() == null) {
            return createParameter(parameterDTO);
        }
        ParameterDTO result = parameterService.save(parameterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, parameterDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /parameters : get all the parameters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of parameters in body
     */
    @GetMapping("/parameters")
    @Timed
    public ResponseEntity<List<ParameterDTO>> getAllParameters(Pageable pageable) {
        log.debug("REST request to get a page of Parameters");
        Page<ParameterDTO> page = parameterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/parameters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /parameters/:id : get the "id" parameter.
     *
     * @param id the id of the parameterDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the parameterDTO, or with status 404 (Not Found)
     */
    @GetMapping("/parameters/{id}")
    @Timed
    public ResponseEntity<ParameterDTO> getParameter(@PathVariable Long id) {
        log.debug("REST request to get Parameter : {}", id);
        ParameterDTO parameterDTO = parameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(parameterDTO));
    }

    /**
     * DELETE  /parameters/:id : delete the "id" parameter.
     *
     * @param id the id of the parameterDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/parameters/{id}")
    @Timed
    public ResponseEntity<Void> deleteParameter(@PathVariable Long id) {
        log.debug("REST request to delete Parameter : {}", id);
        parameterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/parameters?query=:query : search for the parameter corresponding
     * to the query.
     *
     * @param query the query of the parameter search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/parameters")
    @Timed
    public ResponseEntity<List<ParameterDTO>> searchParameters(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Parameters for query {}", query);
        Page<ParameterDTO> page = parameterService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/parameters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
