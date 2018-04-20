package com.micropole.dqb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.micropole.dqb.service.PickleService;
import com.micropole.dqb.web.rest.errors.BadRequestAlertException;
import com.micropole.dqb.web.rest.util.HeaderUtil;
import com.micropole.dqb.service.dto.PickleDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Pickle.
 */
@RestController
@RequestMapping("/api")
public class PickleResource {

    private final Logger log = LoggerFactory.getLogger(PickleResource.class);

    private static final String ENTITY_NAME = "pickle";

    private final PickleService pickleService;

    public PickleResource(PickleService pickleService) {
        this.pickleService = pickleService;
    }

    /**
     * POST  /pickles : Create a new pickle.
     *
     * @param pickleDTO the pickleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pickleDTO, or with status 400 (Bad Request) if the pickle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pickles")
    @Timed
    public ResponseEntity<PickleDTO> createPickle(@RequestBody PickleDTO pickleDTO) throws URISyntaxException {
        log.debug("REST request to save Pickle : {}", pickleDTO);
        if (pickleDTO.getId() != null) {
            throw new BadRequestAlertException("A new pickle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PickleDTO result = pickleService.save(pickleDTO);
        return ResponseEntity.created(new URI("/api/pickles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pickles : Updates an existing pickle.
     *
     * @param pickleDTO the pickleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pickleDTO,
     * or with status 400 (Bad Request) if the pickleDTO is not valid,
     * or with status 500 (Internal Server Error) if the pickleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pickles")
    @Timed
    public ResponseEntity<PickleDTO> updatePickle(@RequestBody PickleDTO pickleDTO) throws URISyntaxException {
        log.debug("REST request to update Pickle : {}", pickleDTO);
        if (pickleDTO.getId() == null) {
            return createPickle(pickleDTO);
        }
        PickleDTO result = pickleService.save(pickleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pickleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pickles : get all the pickles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pickles in body
     */
    @GetMapping("/pickles")
    @Timed
    public List<PickleDTO> getAllPickles() {
        log.debug("REST request to get all Pickles");
        return pickleService.findAll();
        }

    /**
     * GET  /pickles/:id : get the "id" pickle.
     *
     * @param id the id of the pickleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pickleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pickles/{id}")
    @Timed
    public ResponseEntity<PickleDTO> getPickle(@PathVariable Long id) {
        log.debug("REST request to get Pickle : {}", id);
        PickleDTO pickleDTO = pickleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pickleDTO));
    }

    /**
     * DELETE  /pickles/:id : delete the "id" pickle.
     *
     * @param id the id of the pickleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pickles/{id}")
    @Timed
    public ResponseEntity<Void> deletePickle(@PathVariable Long id) {
        log.debug("REST request to delete Pickle : {}", id);
        pickleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pickles?query=:query : search for the pickle corresponding
     * to the query.
     *
     * @param query the query of the pickle search
     * @return the result of the search
     */
    @GetMapping("/_search/pickles")
    @Timed
    public List<PickleDTO> searchPickles(@RequestParam String query) {
        log.debug("REST request to search Pickles for query {}", query);
        return pickleService.search(query);
    }

}
