package pl.dashclever.repairmanagment.repairing.infrastructure.rest.persistance

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.commons.security.WithWorkshopId
import pl.dashclever.repairmanagment.repairing.application.RepairRepository
import pl.dashclever.repairmanagment.repairing.model.Repair
import java.util.UUID

@Repository
class RepairWorkshopSecuredRepository(
    private val entityManager: EntityManager,
    private val currentAccessProvider: CurrentAccessProvider,
    private val securityRecordRepository: EntitySecurityRecordRepository<Repair, UUID, RepairWorkshop>,
    private val repairWorkshopSecuredJpaRepository: RepairWorkshopSecuredJpaRepository
) : RepairRepository {

    @Transactional
    override fun save(repair: Repair) {
        this.entityManager.persist(repair)
        if (isAlreadySecured(repair).not()) {
            val currentAccess: WithWorkshopId = this.currentAccessProvider.currentWorkshopId()
            this.securityRecordRepository.create(RepairWorkshop(currentAccess.workshopId, repair.id))
        }
    }

    override fun anyRunningRepairOfPlanIdIn(planIds: Set<UUID>): Boolean {
        val currentAccess: WithWorkshopId = currentAccessProvider.currentWorkshopId()
        return repairWorkshopSecuredJpaRepository.findAnyRunningRepairWithPlanIdInBelongingToWorkshop(
            planIds,
            currentAccess.workshopId
        )
    }

    private fun isAlreadySecured(repair: Repair): Boolean =
        this.securityRecordRepository.doesSecurityRecordExistFor(repair)
}
