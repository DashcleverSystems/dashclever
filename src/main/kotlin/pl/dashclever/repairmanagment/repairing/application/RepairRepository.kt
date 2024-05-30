package pl.dashclever.repairmanagment.repairing.application

import pl.dashclever.repairmanagment.repairing.model.Repair
import java.util.UUID

interface RepairRepository {

    fun save(repair: Repair)
    fun anyRunningRepairOfPlanIdIn(planIds: Set<UUID>): Boolean
}
