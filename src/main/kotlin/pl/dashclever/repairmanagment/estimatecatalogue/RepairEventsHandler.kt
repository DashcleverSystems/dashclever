package pl.dashclever.repairmanagment.estimatecatalogue

import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.repairing.model.RepairEvent.StartedRepairOfPlan

@Component("EstimateCatalogueRepairEventsHandler")
class RepairEventsHandler(
    private val planRepository: PlanRepository,
    private val estimateRepository: EstimateRepository,
) {

    fun handle(startedRepairOfPlan: StartedRepairOfPlan) {
        val planOfRepair = planRepository.findByIdOrThrow(startedRepairOfPlan.planId)
        val estimate = estimateRepository.findById(planOfRepair.estimateId)
        check(estimate != null) { "Repair has started $startedRepairOfPlan. Tried to update estimate catalogue model. Could not find an estimate" }
        estimate.hasRepairInProgress = true
    }
}
