package pl.dashclever.repairmanagment.plannig.model

import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.repairing.model.RepairEvent.StartedRepairOfPlan

@Component("PlanningRepairEventsHandler")
class RepairEventsHandler(
    private val planRepository: PlanRepository
) {

    fun handle(startedRepairOfPlan: StartedRepairOfPlan) {
        val planOfRepair = planRepository.findByIdOrThrow(startedRepairOfPlan.planId)
        val plansOfSameEstimate = planRepository.findAllByEstimateId(planOfRepair.estimateId)
        for (plan in plansOfSameEstimate) {
            plan.doNotAllowToModify()
        }
    }
}
