package com.micropole.dqb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.micropole.dqb.service.PythonService;
import com.micropole.dqb.web.rest.errors.BadRequestAlertException;
import com.micropole.dqb.web.rest.util.HeaderUtil;
import com.micropole.dqb.web.rest.util.PaginationUtil;
import com.micropole.dqb.service.dto.PythonDTO;
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
 * REST controller for managing Python.
 */
@RestController
@RequestMapping("/api")
public class PythonResource {

    private final Logger log = LoggerFactory.getLogger(PythonResource.class);

    private static final String ENTITY_NAME = "python";

    private final PythonService pythonService;

    public PythonResource(PythonService pythonService) {
        this.pythonService = pythonService;
    }

    /**
     * POST  /pythons : Create a new python.
     *
     * @param pythonDTO the pythonDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pythonDTO, or with status 400 (Bad Request) if the python has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pythons")
    @Timed
    public ResponseEntity<PythonDTO> createPython(@RequestBody PythonDTO pythonDTO) throws URISyntaxException {
        log.debug("REST request to save Python : {}", pythonDTO);
        if (pythonDTO.getId() != null) {
            throw new BadRequestAlertException("A new python cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PythonDTO result = pythonService.save(pythonDTO);
        return ResponseEntity.created(new URI("/api/pythons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pythons : Updates an existing python.
     *
     * @param pythonDTO the pythonDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pythonDTO,
     * or with status 400 (Bad Request) if the pythonDTO is not valid,
     * or with status 500 (Internal Server Error) if the pythonDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pythons")
    @Timed
    public ResponseEntity<PythonDTO> updatePython(@RequestBody PythonDTO pythonDTO) throws URISyntaxException {
        log.debug("REST request to update Python : {}", pythonDTO);
        if (pythonDTO.getId() == null) {
            return createPython(pythonDTO);
        }
        PythonDTO result = pythonService.save(pythonDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pythonDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pythons : get all the pythons.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pythons in body
     */
    @GetMapping("/pythons")
    @Timed
    public ResponseEntity<List<PythonDTO>> getAllPythons(Pageable pageable) {
        log.debug("REST request to get a page of Pythons");
        Page<PythonDTO> page = pythonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pythons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pythons/:id : get the "id" python.
     *
     * @param id the id of the pythonDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pythonDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pythons/{id}")
    @Timed
    public ResponseEntity<PythonDTO> getPython(@PathVariable Long id) {
        log.debug("REST request to get Python : {}", id);
        PythonDTO pythonDTO = pythonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pythonDTO));
    }

    /**
     * DELETE  /pythons/:id : delete the "id" python.
     *
     * @param id the id of the pythonDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pythons/{id}")
    @Timed
    public ResponseEntity<Void> deletePython(@PathVariable Long id) {
        log.debug("REST request to delete Python : {}", id);
        pythonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pythons?query=:query : search for the python corresponding
     * to the query.
     *
     * @param query the query of the python search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/pythons")
    @Timed
    public ResponseEntity<List<PythonDTO>> searchPythons(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Pythons for query {}", query);
        Page<PythonDTO> page = pythonService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pythons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
