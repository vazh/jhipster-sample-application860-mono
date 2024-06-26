package io.github.vazh.service;

import io.github.vazh.domain.*; // for static metamodels
import io.github.vazh.domain.JobHistory;
import io.github.vazh.repository.JobHistoryRepository;
import io.github.vazh.service.criteria.JobHistoryCriteria;
import io.github.vazh.service.dto.JobHistoryDTO;
import io.github.vazh.service.mapper.JobHistoryMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link JobHistory} entities in the database.
 * The main input is a {@link JobHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link JobHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobHistoryQueryService extends QueryService<JobHistory> {

    private static final Logger log = LoggerFactory.getLogger(JobHistoryQueryService.class);

    private final JobHistoryRepository jobHistoryRepository;

    private final JobHistoryMapper jobHistoryMapper;

    public JobHistoryQueryService(JobHistoryRepository jobHistoryRepository, JobHistoryMapper jobHistoryMapper) {
        this.jobHistoryRepository = jobHistoryRepository;
        this.jobHistoryMapper = jobHistoryMapper;
    }

    /**
     * Return a {@link Page} of {@link JobHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobHistoryDTO> findByCriteria(JobHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobHistory> specification = createSpecification(criteria);
        return jobHistoryRepository.findAll(specification, page).map(jobHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobHistory> specification = createSpecification(criteria);
        return jobHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link JobHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobHistory> createSpecification(JobHistoryCriteria criteria) {
        Specification<JobHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), JobHistory_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), JobHistory_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), JobHistory_.endDate));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), JobHistory_.language));
            }
            if (criteria.getJobId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getJobId(), root -> root.join(JobHistory_.job, JoinType.LEFT).get(Job_.id))
                );
            }
            if (criteria.getDepartmentId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getDepartmentId(),
                        root -> root.join(JobHistory_.department, JoinType.LEFT).get(Department_.id)
                    )
                );
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEmployeeId(), root -> root.join(JobHistory_.employee, JoinType.LEFT).get(Employee_.id))
                );
            }
        }
        return specification;
    }
}
