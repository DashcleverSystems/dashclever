package pl.dashclever.repairmanagment.repairing.application

import org.springframework.stereotype.Component
import pl.dashclever.commons.events.DomainEvents
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.repairing.model.Repair
import pl.dashclever.repairmanagment.repairing.model.RepairEvent.StartedRepairOfPlan
import java.util.UUID

@Component
class RepairService(
    private val planRepository: PlanRepository,
    private val repairRepository: RepairRepository,
    private val domainEvents: DomainEvents
) {


    fun createRepairOfPlan(planId: UUID): Repair {
        val plan = planRepository.findByIdOrThrow(planId)
        val repair = Repair(plan.id)
        repairRepository.save(repair)
        domainEvents.publish(StartedRepairOfPlan(repair.id, plan.id))
        return repair
    }
}
