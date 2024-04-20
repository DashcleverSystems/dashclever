package pl.dashclever.repairmanagment.plannig.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import org.hibernate.ObjectNotFoundException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.repairmanagment.plannig.model.Plan
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Repository
class PlanWorkshopSecuredRepository(
    private val entityManager: EntityManager,
    private val securityRecordRepository: EntitySecurityRecordRepository<Plan, UUID, WorkshopPlan>,
    private val planWorkshopSecuredJpaRepository: PlanWorkshopSecuredJpaRepository,
    private val currentAccessProvider: CurrentAccessProvider,
) : PlanRepository {

    @Transactional
    override fun save(plan: Plan): Plan {
        this.entityManager.persist(plan)
        if (isAlreadySecured(plan).not()) {
            val currentAccess = this.currentAccessProvider.currentWorkshopId()
            this.securityRecordRepository.create(WorkshopPlan(currentAccess.workshopId, plan.id))
        }
        return plan
    }

    override fun findById(id: UUID): Plan? {
        val currentAccess = this.currentAccessProvider.currentWorkshopId()
        return this.planWorkshopSecuredJpaRepository.findById(currentAccess.workshopId, id)
    }

    override fun findByIdOrThrow(id: UUID): Plan {
        val currentAccess = this.currentAccessProvider.currentWorkshop()
        val plan = this.planWorkshopSecuredJpaRepository.findById(currentAccess.workshopId, id)
        if (plan == null) {
            logger.error { "Could not find a plan with id: $id for access: $currentAccess" }
            throw ObjectNotFoundException(id, Plan::class.qualifiedName)
        } else {
            return plan
        }
    }

    private fun isAlreadySecured(plan: Plan): Boolean =
        this.securityRecordRepository.doesSecurityRecordExistFor(plan)
}
