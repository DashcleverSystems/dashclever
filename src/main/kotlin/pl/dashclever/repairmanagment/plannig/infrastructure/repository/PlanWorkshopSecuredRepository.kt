package pl.dashclever.repairmanagment.plannig.infrastructure.repository

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.repairmanagment.plannig.model.Plan
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import java.util.UUID

@Repository
class PlanWorkshopSecuredRepository(
    private val entityManager: EntityManager,
    private val securityRecordRepository: EntitySecurityRecordRepository<Plan, UUID, WorkshopPlan>,
    private val planWorkshopSecuredJpaRepository: PlanWorkshopSecuredJpaRepository,
    private val currentAccessProvider: CurrentAccessProvider
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

    private fun isAlreadySecured(plan: Plan): Boolean =
        this.securityRecordRepository.doesSecurityRecordExistFor(plan)
}
