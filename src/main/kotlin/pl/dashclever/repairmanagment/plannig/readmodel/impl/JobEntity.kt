package pl.dashclever.repairmanagment.plannig.readmodel.impl

import pl.dashclever.repairmanagment.plannig.readmodel.JobDto
import java.time.LocalDate
import java.util.UUID

internal data class JobEntity(
    val id: Long,
    val catalogueJobId: Long,
    val manMinutes: Int,
    val planId: UUID,
    val assignedAt: LocalDate?,
    val assignedTo: String?,
    val hour: String?
) {

    internal companion object {
        const val TABLE = "RM_PLANNING_JOB"
        const val ID = "id"
        const val CATALOGUE_JOB_ID = "catalogue_job_id"
        const val MAN_MINUTES = "man_minutes"
        const val ASSIGNED_AT = "assigned_at"
        const val ASSIGNED_TO = "assigned_to"
        const val HOUR = "hour"
        const val PLAN_ID = "plan_id"
    }

    fun toDto(): JobDto {
        return JobDto(
            catalogueJobId = this.catalogueJobId,
            manMinutes = this.manMinutes,
            assignedTo = this.assignedTo,
            assignedAt = this.assignedAt
        )
    }
}
