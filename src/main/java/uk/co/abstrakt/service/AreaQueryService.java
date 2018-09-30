package uk.co.abstrakt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import uk.co.abstrakt.domain.Area;
import uk.co.abstrakt.domain.*; // for static metamodels
import uk.co.abstrakt.repository.AreaRepository;
import uk.co.abstrakt.service.dto.AreaCriteria;

/**
 * Service for executing complex queries for Area entities in the database.
 * The main input is a {@link AreaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Area} or a {@link Page} of {@link Area} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AreaQueryService extends QueryService<Area> {

    private final Logger log = LoggerFactory.getLogger(AreaQueryService.class);

    private final AreaRepository areaRepository;

    public AreaQueryService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    /**
     * Return a {@link List} of {@link Area} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Area> findByCriteria(AreaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Area> specification = createSpecification(criteria);
        return areaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Area} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Area> findByCriteria(AreaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Area> specification = createSpecification(criteria);
        return areaRepository.findAll(specification, page);
    }

    /**
     * Function to convert AreaCriteria to a {@link Specification}
     */
    private Specification<Area> createSpecification(AreaCriteria criteria) {
        Specification<Area> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Area_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Area_.name));
            }
            if (criteria.getCustomersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCustomersId(), Area_.customers, Customer_.id));
            }
        }
        return specification;
    }
}
