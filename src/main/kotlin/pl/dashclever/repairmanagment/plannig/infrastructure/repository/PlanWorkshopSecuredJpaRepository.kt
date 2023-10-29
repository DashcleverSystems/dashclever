package pl.dashclever.repairmanagment.plannig.infrastructure.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.plannig.model.Plan
import java.util.UUID

@Component
interface PlanWorkshopSecuredJpaRepository : Repository<Plan, UUID> {

    @Query("SELECT p FROM Plan p INNER JOIN WorkshopPlan sr ON sr.id.planId = p.id WHERE sr.id.workshopId = :workshopId AND p.id = :planId")
    fun findById(workshopId: UUID, planId: UUID): Plan?
}
