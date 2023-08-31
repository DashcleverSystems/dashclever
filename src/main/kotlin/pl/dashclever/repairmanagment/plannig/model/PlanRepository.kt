package pl.dashclever.repairmanagment.plannig.model

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlanRepository : JpaRepository<pl.dashclever.repairmanagment.plannig.model.Plan, UUID>
