package uk.co.abstrakt.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.co.abstrakt.domain.Area;
import uk.co.abstrakt.repository.AreaRepository;
import uk.co.abstrakt.web.rest.errors.BadRequestAlertException;
import uk.co.abstrakt.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Area.
 */
@RestController
@RequestMapping("/api")
public class AreaResource {

    private final Logger log = LoggerFactory.getLogger(AreaResource.class);

    private static final String ENTITY_NAME = "area";

    private final AreaRepository areaRepository;

    public AreaResource(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    /**
     * POST  /areas : Create a new area.
     *
     * @param area the area to create
     * @return the ResponseEntity with status 201 (Created) and with body the new area, or with status 400 (Bad Request) if the area has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/areas")
    @Timed
    public ResponseEntity<Area> createArea(@Valid @RequestBody Area area) throws URISyntaxException {
        log.debug("REST request to save Area : {}", area);
        if (area.getId() != null) {
            throw new BadRequestAlertException("A new area cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Area result = areaRepository.save(area);
        return ResponseEntity.created(new URI("/api/areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /areas : Updates an existing area.
     *
     * @param area the area to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated area,
     * or with status 400 (Bad Request) if the area is not valid,
     * or with status 500 (Internal Server Error) if the area couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/areas")
    @Timed
    public ResponseEntity<Area> updateArea(@Valid @RequestBody Area area) throws URISyntaxException {
        log.debug("REST request to update Area : {}", area);
        if (area.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Area result = areaRepository.save(area);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, area.getId().toString()))
            .body(result);
    }

    /**
     * GET  /areas : get all the areas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of areas in body
     */
    @GetMapping("/areas")
    @Timed
    public List<Area> getAllAreas() {
        log.debug("REST request to get all Areas");
        return areaRepository.findAll();
    }

    /**
     * GET  /areas/:id : get the "id" area.
     *
     * @param id the id of the area to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the area, or with status 404 (Not Found)
     */
    @GetMapping("/areas/{id}")
    @Timed
    public ResponseEntity<Area> getArea(@PathVariable Long id) {
        log.debug("REST request to get Area : {}", id);
        Optional<Area> area = areaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(area);
    }

    /**
     * DELETE  /areas/:id : delete the "id" area.
     *
     * @param id the id of the area to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/areas/{id}")
    @Timed
    public ResponseEntity<Void> deleteArea(@PathVariable Long id) {
        log.debug("REST request to delete Area : {}", id);

        areaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
