package pl.dashclever.repairmanagment.repairing.infrastructure.rest.persistance

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.repairmanagment.repairing.application.RepairRepository
import pl.dashclever.repairmanagment.repairing.model.Repair
import java.util.UUID

@Repository
class RepairWorkshopSecuredRepository(
    private val entityManager: EntityManager,
    private val currentAccessProvider: CurrentAccessProvider,
    private val securityRecordRepository: EntitySecurityRecordRepository<Repair, UUID, RepairWorkshop>
) : RepairRepository {

    @Transactional
    override fun save(repair: Repair) {
        this.entityManager.persist(repair)
        if (isAlreadySecured(repair).not()) {
            val currentAccess = this.currentAccessProvider.currentWorkshop()
            this.securityRecordRepository.create(RepairWorkshop(currentAccess.workshopId, repair.id))
        }
    }

    private fun isAlreadySecured(repair: Repair): Boolean =
        this.securityRecordRepository.doesSecurityRecordExistFor(repair)
}
