package pl.dashclever.repairmanagment.plannig.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.plannig.model.Plan
import java.time.LocalDate
import java.util.UUID

@Component
interface JobReader : Repository<Plan, UUID> {

    @Query(
        value = """
			SELECT j.catalogue_job_id as catalogueJobId, j.man_minutes as manMinutes,
			j.assigned_to as assignedTo, j.assigned_at as assignedAT
			FROM RM_PLANNING_JOB j
			WHERE j.plan_id = :planId
		""",
        nativeQuery = true
    )
    fun findByPlanId(planId: UUID): Set<JobDto>
}

interface JobDto {
    val catalogueJobId: Long
    val manMinutes: Int
    val assignedTo: String?
    val assignedAt: LocalDate?
}
