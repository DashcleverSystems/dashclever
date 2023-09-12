package pl.dashclever.repairmanagment.plannig.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.plannig.model.Plan
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@Component
interface PlanReader : Repository<Plan, UUID> {

    @Query(
        value =
        """
        SELECT p.id as id, p.estimate_id as estimateId, sum(j.man_minutes) as technicalRepairTimeInMinutes
        FROM RM_PLANNING_PLAN p INNER JOIN RM_PLANNING_JOB j ON j.plan_id = p.id
        WHERE p.id = :id
        GROUP BY p.id
        """,
        nativeQuery = true
    )
    fun findById(id: UUID): Optional<PlanDto>

    @Query(
        value =
        """
        SELECT p.id as id, p.estimate_id as estimateId, sum(j.man_minutes) as technicalRepairTimeInMinutes
        FROM RM_PLANNING_PLAN p INNER JOIN RM_PLANNING_JOB j ON j.plan_id = p.id
        WHERE p.id = :estimateId
        GROUP BY p.id
        """,
        nativeQuery = true
    )
    fun findByEstimateId(estimateId: String): Set<PlanDto>

    @Query(
        value =
        """
        SELECT p.id as id, p.estimate_id as estimateId, sum(j.man_minutes) as technicalRepairTimeInMinutes
        FROM RM_PLANNING_PLAN p
        INNER JOIN RM_PLANNING_JOB j ON j.plan_id = p.id
        WHERE (SELECT min(yj.assigned_at) FROM RM_PLANNING_JOB yj WHERE yj.plan_id = p.id) >= :from
        AND (SELECT min(yj.assigned_at) FROM RM_PLANNING_JOB yj WHERE yj.plan_id = p.id) <= :to
        GROUP BY p.id
        """,
        nativeQuery = true
    )
    fun findByDateRange(from: LocalDate, to: LocalDate): Set<PlanDto>
}

interface PlanDto {
    val id: UUID
    val estimateId: String
    val technicalRepairTimeInMinutes: Int
}
