package pl.dashclever.repairmanagment.plannig.readmodel.impl

import pl.dashclever.repairmanagment.plannig.readmodel.JobDto
import pl.dashclever.repairmanagment.plannig.readmodel.PlanDto
import java.util.UUID
import kotlin.math.ceil
import kotlin.math.roundToInt

internal data class PlanEntity(
    val id: UUID,
    val estimateId: String
) {
    internal companion object {
        const val TABLE = "RM_PLANNING_PLAN"
        const val ID = "id"
        const val ESTIMATE_ID = "estimate_id"
        const val MINUTES_IN_HOUR = 60
        const val WORKING_HOURS_A_DAY = 8
        const val DIFFICULTY_FACTOR = 0.7f
    }

    fun toDto(jobs: Set<JobDto>): PlanDto {
        val jobTimes = jobs.map { it.manMinutes }
        return PlanDto(
            id = this.id.toString(),
            estimateId = this.estimateId,
            technicalRepairTime = calculateTechnicalRepairTime(jobTimes),
            jobs = jobs
        )
    }

    private fun calculateTechnicalRepairTime(jobTimes: List<Int>): Int {
        val timeFloat = jobTimes.reduce { acc, next -> acc + next }
            .toFloat()
            .div(MINUTES_IN_HOUR)
            .div(WORKING_HOURS_A_DAY)
            .div(DIFFICULTY_FACTOR)
        val roundedTop = ceil(timeFloat)
        return roundedTop.roundToInt()
    }
}
