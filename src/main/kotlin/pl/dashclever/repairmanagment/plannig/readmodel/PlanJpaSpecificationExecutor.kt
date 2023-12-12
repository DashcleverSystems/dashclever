package pl.dashclever.repairmanagment.plannig.readmodel

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.Repository
import pl.dashclever.repairmanagment.plannig.model.Plan
import java.util.UUID

interface PlanJpaSpecificationExecutor : Repository<Plan, UUID>, JpaSpecificationExecutor<Plan> {

    fun findById(id: UUID): Plan?
}
