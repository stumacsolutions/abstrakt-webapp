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

import uk.co.abstrakt.domain.Job;
import uk.co.abstrakt.domain.*; // for static metamodels
import uk.co.abstrakt.repository.JobRepository;
import uk.co.abstrakt.service.dto.JobCriteria;

/**
 * Service for executing complex queries for Job entities in the database.
 * The main input is a {@link JobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Job} or a {@link Page} of {@link Job} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobQueryService extends QueryService<Job> {

    private final Logger log = LoggerFactory.getLogger(JobQueryService.class);

    private final JobRepository jobRepository;

    public JobQueryService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Return a {@link List} of {@link Job} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Job> findByCriteria(JobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Job} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Job> findByCriteria(JobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification, page);
    }

    /**
     * Function to convert JobCriteria to a {@link Specification}
     */
    private Specification<Job> createSpecification(JobCriteria criteria) {
        Specification<Job> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Job_.id));
            }
            if (criteria.getWeek() != null) {
                specification = specification.and(buildSpecification(criteria.getWeek(), Job_.week));
            }
            if (criteria.getComplete() != null) {
                specification = specification.and(buildSpecification(criteria.getComplete(), Job_.complete));
            }
            if (criteria.getPaid() != null) {
                specification = specification.and(buildSpecification(criteria.getPaid(), Job_.paid));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCustomerId(), Job_.customer, Customer_.id));
            }
        }
        return specification;
    }
}
