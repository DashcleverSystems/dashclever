package pl.dashclever.repairmanagment.plannig.model

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class PlanService(
    private val planRepository: PlanRepository
) {

    fun removeAssignment(planId: UUID, jobId: Long) {
        val plan = planRepository.findById(planId)!!
        plan.removeAssignment(jobId)
    }
}
