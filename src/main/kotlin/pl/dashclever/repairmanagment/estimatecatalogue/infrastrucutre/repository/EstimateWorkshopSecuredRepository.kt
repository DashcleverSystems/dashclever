package pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import pl.dashclever.spring.security.SpringCurrentAccessProvider
import java.util.UUID

@Component
class EstimateWorkshopSecuredRepository(
    private val springCurrentAccessProvider: SpringCurrentAccessProvider,
    private val entityManager: EntityManager,
    private val securityRecordRepository: EntitySecurityRecordRepository<Estimate, UUID, WorkshopEstimate>,
    private val estimateWorkshopSecuredJpaReadRepository: EstimateWorkshopSecuredJpaRepository
) : EstimateRepository {

    @Transactional
    override fun save(estimate: Estimate): Estimate {
        this.entityManager.persist(estimate)
        if (isAlreadySecured(estimate)) {
            return estimate
        }
        val currentAccess = this.springCurrentAccessProvider.currentWorkshop()
        this.securityRecordRepository.create(WorkshopEstimate(currentAccess.workshopId, estimate.id))
        return estimate
    }

    private fun isAlreadySecured(estimate: Estimate): Boolean =
        this.securityRecordRepository.doesSecurityRecordExistFor(estimate)

    override fun findById(id: UUID): Estimate? {
        val currentAccess = this.springCurrentAccessProvider.currentWorkshop()
        return this.estimateWorkshopSecuredJpaReadRepository.findById(currentAccess.workshopId, id)
    }

    override fun findAll(specification: Specification<Estimate>, pageable: Pageable): Page<Estimate> {
        val currentAccess = this.springCurrentAccessProvider.currentWorkshop()
        val spec = specification.and(joinSecurityRecords()).and(withWorkshopId(currentAccess.workshopId))
        return this.estimateWorkshopSecuredJpaReadRepository.findAll(spec, pageable)
    }

    override fun findAll(pageable: Pageable): Page<Estimate> {
        val currentAccess = this.springCurrentAccessProvider.currentWorkshop()
        return this.estimateWorkshopSecuredJpaReadRepository.findAll(currentAccess.workshopId, pageable)
    }

    override fun existsByEstimateId(estimateId: String): Boolean {
        val currentAccess = this.springCurrentAccessProvider.currentWorkshop()
        return this.estimateWorkshopSecuredJpaReadRepository.existsByEstimateId(currentAccess.workshopId, estimateId)
    }

    @Transactional
    override fun deleteById(id: UUID) {
        val currentAccess = this.springCurrentAccessProvider.currentWorkshop()
        this.securityRecordRepository.deleteByEntityId(id)
        return this.estimateWorkshopSecuredJpaReadRepository.deleteById(currentAccess.workshopId, id)
    }

    private companion object {

        fun joinSecurityRecords(): Specification<Estimate> {
            return Specification { root, query, criteriaBuilder ->
                val securityRecordRoot: Root<WorkshopEstimate> = query.from(WorkshopEstimate::class.java)
                criteriaBuilder.equal(
                    root.get<UUID>("id"),
                    securityRecordRoot.get<WorkshopEstimate.ComposePk>("id").get<UUID>("estimateId")
                )
            }
        }

        fun withWorkshopId(workshopId: UUID): Specification<Estimate> {
            return Specification { _, query, criteriaBuilder ->
                val securityRecordRoot: Root<WorkshopEstimate> = query.from(WorkshopEstimate::class.java)
                criteriaBuilder.equal(
                    securityRecordRoot.get<WorkshopEstimate.ComposePk>("id").get<UUID>("workshopId"),
                    workshopId
                )
            }
        }
    }
}
