package pl.dashclever.repairmanagment.repairing.infrastructure.rest.persistance

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.repairing.model.Repair
import java.util.UUID

@Component
interface RepairWorkshopSecuredJpaRepository : Repository<Repair, UUID> {

    @Query(
        """
        SELECT COUNT(r.id) > 0 FROM Repair r
            INNER JOIN RepairWorkshop sr ON sr.id.repairId = r.id
            WHERE r.planId in :planIds AND sr.id.workshopId = :workshopId
        """
    )
    fun findAnyRunningRepairWithPlanIdInBelongingToWorkshop(planIds: Set<UUID>, workshopId: UUID): Boolean
}
